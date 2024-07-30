package top.woaibocai.blog.service.impl;

import com.alibaba.druid.util.StringUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Service;
import top.woaibocai.blog.mapper.EmailContentMapper;
import top.woaibocai.blog.service.EmailService;
import top.woaibocai.common.utils.RandomUtils;
import top.woaibocai.common.utils.TimeUtils;
import top.woaibocai.model.Do.KeyValue;
import top.woaibocai.model.Enum.RedisKeyEnum;
import top.woaibocai.model.Enum.ResultCodeEnum;
import top.woaibocai.model.common.Result;
import top.woaibocai.model.entity.blog.Article;
import top.woaibocai.model.entity.email.Content;
import top.woaibocai.model.vo.blog.article.BlogArticleVo;
import top.woaibocai.model.vo.email.EmailPublicVo;
import top.woaibocai.model.vo.email.LikebocaiPageVo;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @program: woaibocai-parent
 * @description: 时光邮局接口实现类
 * @author: LikeBocai
 * @create: 2024/7/22 15:14
 **/
@Service
public class EmailServiceImpl implements EmailService {

    @Resource
    private EmailContentMapper emailContentMapper;
    @Resource
    private RedisTemplate<String,String> redisTemplateString;
    @Resource
    private HashOperations<String,String,String> hashOperationSSS;
    ObjectMapper objectMapper = new ObjectMapper();
    @Override
    public Result<LikebocaiPageVo<EmailPublicVo>> publicEmailList(String type, Integer current, Integer size) {
        // 根据 type 来判断要去那个 key 中 取数据分页
        Map<String,Result<LikebocaiPageVo<EmailPublicVo>>> map = new HashMap<>();
        map.put("selection", selection(current,size));
        map.put("delivery",delivery(current,size));
        map.put("writing",writing(current,size));
        return map.get(type);
    }

    @Override
    public Result<EmailPublicVo> publicEmailText(String emailId) {
        Long total = redisTemplateString.opsForZSet().size(RedisKeyEnum.EMAIL_PUBLIC_ZSET_SELECTION);
        if (total != null && total.equals(0L)) {
            // 初始化
            // 查询 内容表 即刻公开 likeCount、id
            List<KeyValue<String, Long>> idAndCount = emailContentMapper.getEmailIdAndLikeCount();
            Set<ZSetOperations.TypedTuple<String>> typedTupleSet = new HashSet<>();
            idAndCount.forEach(kv -> typedTupleSet.add(new DefaultTypedTuple<>(kv.getK(), kv.getV().doubleValue())));
            total = redisTemplateString.opsForZSet().add(RedisKeyEnum.EMAIL_PUBLIC_ZSET_SELECTION, typedTupleSet);
        }
        Long randomed = Long.valueOf(RandomUtils.randomBetween(total, 0L));
        Set<ZSetOperations.TypedTuple<String>> typedTupleSet = redisTemplateString.opsForZSet().reverseRangeWithScores(RedisKeyEnum.EMAIL_PUBLIC_ZSET_SELECTION, randomed, randomed);
        // 看看 redis 有没有
        Map<Object, Object> entries = redisTemplateString.opsForHash().entries(RedisKeyEnum.EMAIL_PUBLIC_HASH.common(emailId));
        if (entries.size() == 0 ) {
            // 先查看 数据库中 是否有
            Content content = emailContentMapper.getEmailPublicVoById(emailId);
            if (content == null) {
                return Result.build(null,500,"此邮件不存在！");
            }
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
            Map<String, String> map = objectMapper.convertValue(emailPublicVo, new TypeReference<>() {});
            redisTemplateString.opsForHash().putAll(RedisKeyEnum.EMAIL_PUBLIC_HASH.common(emailId),map);
            redisTemplateString.expire(RedisKeyEnum.EMAIL_PUBLIC_HASH.common(emailId), Long.parseLong(RandomUtils.randomBetween(1440L,5L)), TimeUnit.MINUTES);
            // 查询 用户信息 url nickname
            if (!StringUtils.isEmpty(content.getUserId())) {
                KeyValue<String,String> userInfo = emailContentMapper.getUserInfoByUserId(emailPublicVo.getUserId());
                emailPublicVo.setUrl(userInfo.getV());
                emailPublicVo.setNickName(userInfo.getK());
            } else {
                emailPublicVo.setNickName("匿名用户");
            }
            // 生成 随机数
            emailPublicVo.setId(typedTupleSet.iterator().next().getValue());
            return Result.build(emailPublicVo,ResultCodeEnum.SUCCESS);
        }

        EmailPublicVo emailPublicVo = objectMapper.convertValue(entries, EmailPublicVo.class);
        // 查询 用户信息 url nickname
        if (!StringUtils.isEmpty(emailPublicVo.getUserId())) {
            KeyValue<String,String> userInfo = emailContentMapper.getUserInfoByUserId(emailPublicVo.getUserId());
            emailPublicVo.setUrl(userInfo.getV());
            emailPublicVo.setNickName(userInfo.getK());
        } else {
            emailPublicVo.setNickName("匿名用户");
        }
        // 生成 随机数
        emailPublicVo.setId(typedTupleSet.iterator().next().getValue());
        return Result.build(emailPublicVo,ResultCodeEnum.SUCCESS);
    }

    @Override
    public Result publicEmailIndex() {
        Long total = redisTemplateString.opsForZSet().size(RedisKeyEnum.EMAIL_PUBLIC_ZSET_SELECTION);
        if (total != null && total.equals(0L)) {
            // 初始化
            // 查询 内容表 即刻公开 likeCount、id
            List<KeyValue<String, Long>> idAndCount = emailContentMapper.getEmailIdAndLikeCount();
            Set<ZSetOperations.TypedTuple<String>> typedTupleSet = new HashSet<>();
            idAndCount.forEach(kv -> typedTupleSet.add(new DefaultTypedTuple<>(kv.getK(), kv.getV().doubleValue())));
            total = redisTemplateString.opsForZSet().add(RedisKeyEnum.EMAIL_PUBLIC_ZSET_SELECTION, typedTupleSet);
        }
        // TODO: 随机数方法写的有问题 边界问题没有 考虑到
//        Long randomed = Long.valueOf(RandomUtils.randomBetween(total - 1L, 2L));
        List<String> strings = redisTemplateString.opsForZSet().randomMembers(RedisKeyEnum.EMAIL_PUBLIC_ZSET_SELECTION, 3);
//        List<String> ids = new ArrayList<>();
//        for (ZSetOperations.TypedTuple<String> typedTuple : typedTupleSet) {
//            ids.add(typedTuple.getValue());
//        }
        List<EmailPublicVo> emailPublicVos = getEmailPublicListByEmailIdList(strings);
        for (EmailPublicVo emailPublicVo : emailPublicVos) {
            // 查询 用户信息 url nickname
            if (!StringUtils.isEmpty(emailPublicVo.getUserId())) {
                KeyValue<String,String> userInfo = emailContentMapper.getUserInfoByUserId(emailPublicVo.getUserId());
                emailPublicVo.setUrl(userInfo.getV());
                emailPublicVo.setNickName(userInfo.getK());
                // 处理字符串
//                emailPublicVo.getContent();
                Pattern pattern = Pattern.compile("src=\"([^\"]*)\"");
                Matcher matcher = pattern.matcher(emailPublicVo.getContent());
                if(matcher.find()) {
                    String srcValue = matcher.group(1);
                    emailPublicVo.setImgUrl(srcValue);
                }
                String html = emailPublicVo.getContent().replaceAll("<.*?>", "").replaceAll("#","");
                if(!StringUtils.isEmpty(html)) {
                    emailPublicVo.setContent(html);
                }
            } else {
                Pattern pattern = Pattern.compile("src=\"([^\"]*)\"");
                Matcher matcher = pattern.matcher(emailPublicVo.getContent());
                if(matcher.find()) {
                    String srcValue = matcher.group(1);
                    emailPublicVo.setImgUrl(srcValue);
                }
                String html = emailPublicVo.getContent().replaceAll("<.*?>", "").replaceAll("#","");
                if(!StringUtils.isEmpty(html)) {
                    emailPublicVo.setContent(html);
                }
                emailPublicVo.setNickName("匿名用户");
            }
        }
        // 有多少信 投递了
        Long yesNumber = emailContentMapper.getYesDeliveryTotal();
        // 有多少信 还没投递
        Long noNumber = emailContentMapper.NoDeliveryTotal();
        Map<String,Object> map = new HashMap<>();
        map.put("yesNumber",yesNumber);
        map.put("noNumber",noNumber);
        map.put("data",emailPublicVos);
        return Result.build(map,ResultCodeEnum.SUCCESS);
    }

    // 分页获取标标
    private Map<String, Long> getIndex(Long current, Long size, Long total) {
        Map<String, Long> index = new HashMap<>();
        Long realSize = total / current;
        if (size > (long) Math.ceil(realSize)) {
            index.put("current",0L);
            index.put("size",current - 1L);
            return index;
        }
        if (size.equals ((long) Math.ceil(realSize))) {
            index.put("current",current * (size - 1L));
            index.put("size",total - 1L);
            return index;
        }
        index.put("current",current * (size - 1L));
        index.put("size",(current * size) - 1L);
        return index;
    }
    private List<EmailPublicVo> getEmailPublicListByEmailIdList(List<String> emailIdList) {
        List<EmailPublicVo> data = new ArrayList<>();
        redisTemplateString.executePipelined((RedisCallback<Void>) connection -> {
            emailIdList.forEach(obj -> {
                Map<Object, Object> entries = redisTemplateString.opsForHash().entries(RedisKeyEnum.EMAIL_PUBLIC_HASH.common(obj));
                // 如果信件内容不在那就初始化他！
                if (entries.size() == 0 ) {
                    // 初始 该邮件到 redis 上 并返回 一个对象回来
                    EmailPublicVo emailPublicVo = initEmailByRedis(obj);
                    data.add(emailPublicVo);
                } else {
                    EmailPublicVo emailPublicVo = objectMapper.convertValue(entries, EmailPublicVo.class);
                    data.add(emailPublicVo);
                }
            });
            return null;
        });
        return data;
    }
    private EmailPublicVo initEmailByRedis(String emailId) {
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

    private Result<LikebocaiPageVo<EmailPublicVo>> selection(Integer current, Integer size) {
        // 获取 selection 的有序集合
        Long total = redisTemplateString.opsForZSet().size(RedisKeyEnum.EMAIL_PUBLIC_ZSET_SELECTION);
        if (total != null && total.equals(0L)) {
            // 初始化
            // 查询 内容表 即刻公开 likeCount、id
            List<KeyValue<String, Long>> idAndCount = emailContentMapper.getEmailIdAndLikeCount();
            Set<ZSetOperations.TypedTuple<String>> typedTupleSet = new HashSet<>();
            idAndCount.forEach(kv -> typedTupleSet.add(new DefaultTypedTuple<>(kv.getK(), kv.getV().doubleValue())));
            total = redisTemplateString.opsForZSet().add(RedisKeyEnum.EMAIL_PUBLIC_ZSET_SELECTION, typedTupleSet);
        }
        // 根据 current size total 进行分页
        Map<String,Long> index = getIndex(current.longValue(), size.longValue(), total);
        Set<ZSetOperations.TypedTuple<String>> typedTupleSet = redisTemplateString.opsForZSet().reverseRangeWithScores(RedisKeyEnum.EMAIL_PUBLIC_ZSET_SELECTION, index.get("current"), index.get("size"));
        List<KeyValue<String, Long>> emailIdList = new ArrayList<>();
        for (ZSetOperations.TypedTuple<String> typedTuple : typedTupleSet) {
            emailIdList.add(new KeyValue<String, Long>(typedTuple.getValue(),typedTuple.getScore().longValue()));
        }

        List<EmailPublicVo> data = getEmailPublicListByEmailIdList(emailIdList.stream().map(KeyValue::getK).toList());
        // 给点赞数赋值 来自 zset中的排名
        for (int i=0; i<data.size(); i++) {
            data.get(i).setLikeCount(emailIdList.get(i).getV().toString());
        }
        LikebocaiPageVo<EmailPublicVo> result = new LikebocaiPageVo<>();
        result.setCurrent(current);
        result.setSize(size);
        result.setTotal(total.intValue());
        result.setPageData(data);
        return Result.build(result, ResultCodeEnum.SUCCESS);
    }




    private Result<LikebocaiPageVo<EmailPublicVo>> delivery(Integer current, Integer size) {
        Long total = redisTemplateString.opsForList().size(RedisKeyEnum.EMAIL_PUBLIC_LIST_DELIVERY);
        if (total != null && total.equals(0L)) {
            // 初始化数据
            List<String> ids = emailContentMapper.getNewDeliveryByDateOfYW();
            total = redisTemplateString.opsForList().rightPushAll(RedisKeyEnum.EMAIL_PUBLIC_LIST_DELIVERY, ids);
        }
        // 分页数据获取角标
        Map<String, Long> index = getIndex(current.longValue(), size.longValue(), total);
        List<String> needIds = redisTemplateString.opsForList().range(RedisKeyEnum.EMAIL_PUBLIC_LIST_DELIVERY, index.get("current"), index.get("size"));

        List<EmailPublicVo> data = getEmailPublicListByEmailIdList(needIds);

        LikebocaiPageVo<EmailPublicVo> result = new LikebocaiPageVo<>();
        result.setCurrent(current);
        result.setSize(size);
        result.setTotal(total.intValue());
        result.setPageData(data);
        return Result.build(result, ResultCodeEnum.SUCCESS);
    }
    private Result<LikebocaiPageVo<EmailPublicVo>> writing(Integer current, Integer size) {
        Long total = redisTemplateString.opsForList().size(RedisKeyEnum.EMAIL_PUBLIC_LIST_WRITING);
        if (total != null && total.equals(0L)) {
            // 初始化数据
            List<String> ids = emailContentMapper.getNewWritingByDateOfY();
            total = redisTemplateString.opsForList().rightPushAll(RedisKeyEnum.EMAIL_PUBLIC_LIST_WRITING, ids);
        }
        // 分页数据获取角标
        Map<String, Long> index = getIndex(current.longValue(), size.longValue(), total);
        List<String> needIds = redisTemplateString.opsForList().range(RedisKeyEnum.EMAIL_PUBLIC_LIST_WRITING, index.get("current"), index.get("size"));

        List<EmailPublicVo> data = getEmailPublicListByEmailIdList(needIds);

        LikebocaiPageVo<EmailPublicVo> result = new LikebocaiPageVo<>();
        result.setCurrent(current);
        result.setSize(size);
        result.setTotal(total.intValue());
        result.setPageData(data);
        return Result.build(result, ResultCodeEnum.SUCCESS);
    }
}
