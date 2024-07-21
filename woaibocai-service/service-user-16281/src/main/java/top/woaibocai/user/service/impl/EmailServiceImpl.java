package top.woaibocai.user.service.impl;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.woaibocai.common.utils.RandomUtils;
import top.woaibocai.model.Enum.RabbitMqEnum;
import top.woaibocai.model.common.Result;
import top.woaibocai.model.Enum.ResultCodeEnum;
import top.woaibocai.model.dto.email.ContentDto;
import top.woaibocai.model.entity.email.Content;
import top.woaibocai.user.mapper.EmailContentMapper;
import top.woaibocai.user.service.EmailService;
import top.woaibocai.user.utils.IpThrottlingUtils;
import top.woaibocai.user.utils.RabbitMqUtils;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

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
        emailContentMapper.emailWritingEmail(content);


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
        return Result.build(null, ResultCodeEnum.SUCCESS);
    }
}
