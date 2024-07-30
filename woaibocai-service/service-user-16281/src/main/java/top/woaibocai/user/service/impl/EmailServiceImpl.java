package top.woaibocai.user.service.impl;

import com.alibaba.druid.util.StringUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.woaibocai.common.utils.RandomUtils;
import top.woaibocai.common.utils.TimeUtils;
import top.woaibocai.model.Enum.RabbitMqEnum;
import top.woaibocai.model.Enum.RedisKeyEnum;
import top.woaibocai.model.common.Result;
import top.woaibocai.model.Enum.ResultCodeEnum;
import top.woaibocai.model.dto.email.ContentDto;
import top.woaibocai.model.entity.email.Content;
import top.woaibocai.model.vo.email.EmailPublicVo;
import top.woaibocai.user.mapper.EmailContentMapper;
import top.woaibocai.user.service.EmailService;
import top.woaibocai.user.utils.IpThrottlingUtils;
import top.woaibocai.user.utils.RabbitMqUtils;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @program: woaibocai-parent
 * @description: 关于时光邮局用户接口的实现类
 * @author: LikeBocai
 * @create: 2024/7/18 13:27
 **/
@Service
@Slf4j
public class EmailServiceImpl implements EmailService {
    @Resource
    private EmailContentMapper emailContentMapper;
    @Resource
    private RabbitMqUtils rabbitMqUtils;
    @Resource
    private IpThrottlingUtils ipThrottlingUtils;
    @Resource
    private RedisTemplate<String,String> redisTemplateString;
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result emailWritingEmail(ContentDto contentDto,String ip) {
        // 生成emailId
        // 生成7位 数字 + 字母的 随机字符串
        String emailId = "em" +
                new SimpleDateFormat("yyyy-MM-dd").format(new Date()).toString().replace("-", "").substring(2, 8) +
                "-" + RandomUtils.randomStringAndNumber(7);

        // 将 Date 时间转化为 LocalDateTime
        Instant instant = contentDto.getDeliveryTime().toInstant();
        ZoneId zoneId = ZoneId.systemDefault();
        LocalDateTime localDateTime = instant.atZone(zoneId).toLocalDateTime();

        // 插入数据库
        Content content = new Content();
        content.setId(emailId);
        content.setUserId(contentDto.getUserId());
        content.setTitle(contentDto.getTitle());
        content.setSenderEmail(contentDto.getSenderEmail());
        content.setRecipientEmail(contentDto.getRecipientEmail());
        content.setIsPublic(contentDto.getIsPublic());
        content.setContent(contentDto.getContent());
        content.setDeliveryTime(localDateTime);

        // 没活硬整(这点用户量我不信在一天之内，有人能摇出来一摸一样的七位随机数)，其实这里要用 Spring-retry 或 用注解写重试
        try {
            emailContentMapper.emailWritingEmail(content);
        } catch (Exception e) {
            String newEmailId = "em" +
                    new SimpleDateFormat("yyyy-MM-dd").format(new Date()).toString().replace("-", "").substring(2, 8) +
                    "-" + RandomUtils.randomStringAndNumber(7);
            content.setId(newEmailId);
            try {
                emailContentMapper.emailWritingEmail(content);
            } catch (Exception ex) {
                String nnewEmailId = "em" +
                        new SimpleDateFormat("yyyy-MM-dd").format(new Date()).toString().replace("-", "").substring(2, 8) +
                        "-" + RandomUtils.randomStringAndNumber(7);
                content.setId(nnewEmailId);
                try {
                    emailContentMapper.emailWritingEmail(content);
                } catch (Exception exc) {
                    return Result.build(null,500,"行了别发邮件，你现在立刻马上下楼买一张双色球，你一定能中！中了咱俩一人一半，没中算你倒霉！");
                }
            }
        }


        long epochMilli = instant.toEpochMilli() - new Date().getTime();
        // 如果 邮寄时间的间隔 小与 4294967000L 。那就让过期时间在 3000L - epochMilli 之间随机；大于 那就在 3000L - 4294967000L 之间随机
        Long max = epochMilli - 4294967000L < 0 ? epochMilli : 4294967000L;
        String randomed = RandomUtils.randomBetween(max, 0L);
        // 将 信件id 和 到达时间的时间戳 发送到 rabbitMQ 上
        rabbitMqUtils.convertAndSend(RabbitMqEnum.AWAIT_LETTER,emailId + ";" + instant.toEpochMilli(),message -> {
            message.getMessageProperties().setExpiration(randomed);
            return message;
        });
        //  ip 限流
        ipThrottlingUtils.increment(ip);
        log.info("{}: 已发送到 exchange.email.deliver.letter 交换机上，将于 {} 投递，初始过期时间为：{}毫秒", emailId, localDateTime, randomed);
        // 根据 IsDelivery 决定放不放到 zset 和 最新书写中
        if ("Y".equals(contentDto.getIsPublic())) {
            redisTemplateString.opsForZSet().add(RedisKeyEnum.EMAIL_PUBLIC_ZSET_SELECTION,emailId,0);
            redisTemplateString.opsForList().leftPush(RedisKeyEnum.EMAIL_PUBLIC_LIST_WRITING,emailId);
            log.info("已将即刻公开的信件 {} 放入最新书写、zset的 redis 中",emailId);
        }
        log.info("用户{}写了一封信，信件id为：{},ip地址为：{}", !StringUtils.isEmpty(contentDto.getUserId()) ? contentDto.getUserId() : "匿名用户",emailId,ip);
        return Result.build(null, ResultCodeEnum.SUCCESS);
    }

    @Override
    public Result emailLike(String emailId) {
        // 改数据库
        emailContentMapper.updateLikeCount(emailId);
        // 传到 redis上
        // 1.zset
        redisTemplateString.opsForZSet().incrementScore(RedisKeyEnum.EMAIL_PUBLIC_ZSET_SELECTION,emailId,1);
        // 2.hash：emailId
        // 初始化
        Object o = redisTemplateString.opsForHash().get(RedisKeyEnum.EMAIL_PUBLIC_HASH.common(emailId), "likeCount");
        if (o == null) {
            EmailPublicVo emailPublicVo = initEmailByRedis(emailId);
            o = emailPublicVo.getLikeCount();
        }
        System.out.println(Long.parseLong((String) o));
        redisTemplateString.opsForHash().put(RedisKeyEnum.EMAIL_PUBLIC_HASH.common(emailId),"likeCount",String.valueOf(Long.parseLong((String) o) + 1L));
        return Result.build(null, ResultCodeEnum.SUCCESS);
    }
    private EmailPublicVo initEmailByRedis(String emailId) {
        ObjectMapper objectMapper = new ObjectMapper();
        Content content = emailContentMapper.getEmailPublicVoById(emailId);
        // 计算时间 转为 string
        EmailPublicVo emailPublicVo = new EmailPublicVo();
        emailPublicVo.setId(emailId);
        emailPublicVo.setUserId(content.getUserId());
        emailPublicVo.setIsPublic(content.getIsPublic());
        emailPublicVo.setIsDelivery(content.getIsDelivery());
        emailPublicVo.setTitle(content.getTitle());
        emailPublicVo.setContent(content.getContent());
        emailPublicVo.setWritingDate(content.getWritingEmailTime().toString());
        emailPublicVo.setDeliveryDate(content.getDeliveryTime().toString());
        emailPublicVo.setLikeCount(content.getLikeCount().toString());
        emailPublicVo.setUseTime(TimeUtils.localDateTimeBetween(content.getWritingEmailTime(), content.getDeliveryTime()));
        // 推送到redis上
        Map<String, String> map = objectMapper.convertValue(emailPublicVo, new TypeReference<>() {});
        redisTemplateString.opsForHash().putAll(RedisKeyEnum.EMAIL_PUBLIC_HASH.common(emailId),map);
        redisTemplateString.expire(RedisKeyEnum.EMAIL_PUBLIC_HASH.common(emailId), Long.parseLong(RandomUtils.randomBetween(1440L,5L)), TimeUnit.MINUTES);
        return emailPublicVo;
    }
}
