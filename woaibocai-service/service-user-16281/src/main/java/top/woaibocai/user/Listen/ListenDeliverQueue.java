package top.woaibocai.user.Listen;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import top.woaibocai.common.utils.RandomUtils;
import top.woaibocai.model.Enum.RabbitMqEnum;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import top.woaibocai.model.Enum.RedisKeyEnum;
import top.woaibocai.model.entity.email.Content;
import top.woaibocai.model.entity.email.ErrLog;
import top.woaibocai.user.mapper.EmailContentMapper;
import top.woaibocai.user.mapper.EmailErrLogMapper;
import top.woaibocai.user.utils.QQEmailUtils;
import top.woaibocai.user.utils.RabbitMqUtils;

import java.io.IOException;
import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @program: woaibocai-parent
 * @description: 监听投递队列，将未到达投递时间的信件续期
 * @author: LikeBocai
 * @create: 2024/7/20 13:21
 **/
@Configuration
@Slf4j
public class ListenDeliverQueue {
    @Resource
    private EmailErrLogMapper emailErrLogMapper;
    @Resource
    private EmailContentMapper emailContentMapper;
    @Resource
    private RabbitMqUtils rabbitMqUtils;
    @Resource
    private RedisTemplate<String, Long> redisTemplateLong;
    @Resource
    private QQEmailUtils qqEmailUtils;
    // 监听 计算队列
    @RabbitListener(queues = {RabbitMqEnum.QUEUE_EMAIL_COMPUTE_LETTER})
    public void listenEmailCompute(String dataString, Message message, Channel channel) throws IOException {
        // 获取当前消息的deliveryTag
        long deliveryTag = message.getMessageProperties().getDeliveryTag();

        // 核心业务： 1.查看是否到达投递时间，到达就放入 发邮件的队列(queue.email.deliver.letter)中，没到达就 计算时间 然后重新放回 queue.email.await.letter
        // 把 信件id 和 过期时间取出来
        String[] split = dataString.split(";");
        String emailId = split[0];
        long timeOut = Long.parseLong(split[1]);
        // 计算是否过期
        long nowTime = Instant.now().toEpochMilli();
        long size = nowTime - timeOut;
        if (size < 0L) {
            // 小于说明 没到时间，应该放到 queue.email.await.letter
            try {
                // 比较 差值 和 4294967000L 的 大小
                long max = 4294967000L - Math.abs(size) > 0 ? Math.abs(size) : 4294967000L;
                String randomed = RandomUtils.randomBetween(max, 0L);

                // 发送消息
                rabbitMqUtils.convertAndSend(RabbitMqEnum.AWAIT_LETTER,dataString,message1 -> {
                    message1.getMessageProperties().setExpiration(randomed);
                    message1.getMessageProperties().setContentEncoding("UTF-8");
                    return message1;
                });

                // 确认消息消费
                channel.basicAck(deliveryTag,false);
//                log.info("{} 未到投递时间，将它放回 queue.email.await.letter 队列，随机数时间为：{}",emailId,randomed);
            } catch (IOException e) {
                Boolean redelivered = message.getMessageProperties().getRedelivered();
                if (!redelivered) {
                    channel.basicNack(deliveryTag,false,true);
                    log.info("重新放回 --> {} 未到投递时间，但是 queue.email.compute.letter 抛出异常，现在将他重新放回队列再试一次！deliveryTag：{}",emailId,deliveryTag);
                } else {
                    // 插入数据库
                    ErrLog errLog = new ErrLog();
                    errLog.setId(UUID.randomUUID().toString().replace("-",""));
                    errLog.setEmailId(emailId);
                    errLog.setErrReason("重试失败:" + e.getMessage());
                    emailErrLogMapper.listenEmailComputeErrLog(errLog);
                    channel.basicNack(deliveryTag,false,false);
                    log.info("重试失败 --> {} 未到投递时间，已将日志写入数据库",emailId);
                }
                throw new RuntimeException(e);
            }
        } else {
            // 大于等于说明 到时间/超出时间了 应该放到 发邮件的队列(queue.email.deliver.letter)
            rabbitMqUtils.convertAndSend(RabbitMqEnum.DELIVER_LETTER,dataString);
            // 确认消息消费
            channel.basicAck(deliveryTag,false);
            log.info("{} 已到投递时间，将它放回 queue.email.deliver.letter 队列",emailId);
        }
    }

    // 给 收信人发信/发信人发信 (用 是否投递 来判断给谁发)
    @RabbitListener(queues = {RabbitMqEnum.QUEUE_EMAIL_DELIVER_LETTER})
    public void listenEmailDelivery(String dataString, Message message, Channel channel) throws InterruptedException, IOException {
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        String[] split = dataString.split(";");
        String emailId = split[0];
        // 查看 邮件发送是否 超过api的限制 每天500 每秒 20封 每个邮箱在 1小时内 最多10封

        // key 不存在时设置一个 key不存在 true ，存在为false
        Boolean datMax = redisTemplateLong.opsForValue().setIfAbsent("email:day:max", 0L,24, TimeUnit.HOURS);
        if (datMax) {
            log.info("每24小时发送500封信的锁已重置！");
        } else {
            Long dayNum = redisTemplateLong.opsForValue().get(RedisKeyEnum.EMAIL_DAY_MAX);
            if (dayNum >= 500L) {
                // 推迟投递的 放在 await中
                String randomed = RandomUtils.randomBetween(86460000L, 86400000L);
                rabbitMqUtils.convertAndSend(RabbitMqEnum.AWAIT_LETTER,dataString,message1 -> {

                    message1.getMessageProperties().setExpiration(randomed);
                    return message1;
                });
                log.info("{}: 因系统每日发信数量超载而推后24h + {} 毫秒发送",emailId,randomed);
                // 确认消费
                channel.basicAck(deliveryTag,false);
                return;
            }
        }

        // 每秒 20 封
        Boolean sMax = redisTemplateLong.opsForValue().setIfAbsent(RedisKeyEnum.EMAIL_S_MAX, 0L, 1100, TimeUnit.MILLISECONDS);
        if (sMax) {
            log.info("每秒发送20封信的锁已重置！");
        } else {
            Long sNum = redisTemplateLong.opsForValue().get(RedisKeyEnum.EMAIL_S_MAX);
            if (sNum >= 20L) {
                TimeUnit.MILLISECONDS.sleep(1000);
            }
        }


        // 根据 emailId 查询邮件信息
        Content content = emailContentMapper.selectEmailById(emailId);

        // 查不到邮件 错误 写入数据库
        if (content == null) {
            ErrLog errLog = new ErrLog();
            errLog.setId(UUID.randomUUID().toString().replace("-",""));
            errLog.setEmailId(emailId);
            errLog.setErrReason("在listenEmailDelivery中 根据" + emailId + "没查到数据");
            emailErrLogMapper.listenEmailComputeErrLog(errLog);
            // 确认消费
            channel.basicAck(deliveryTag,false);
            return;
        }
        // 发送的信箱
        if ("N".equals(content.getIsDelivery())) {
            rabbitMqUtils.convertAndSend(RabbitMqEnum.DELIVER_LETTER,dataString);
            log.info("{}:给发信人通知",emailId);
        } else {
            // 不存在此邮箱
            try {
                qqEmailUtils.noticeDeliverySendHtml(content);
            } catch (Exception e) {
                ErrLog errLog = new ErrLog();
                errLog.setId(UUID.randomUUID().toString().replace("-",""));
                errLog.setEmailId(emailId);
                errLog.setErrReason("发信人邮箱不存在？" + e.getMessage());
                emailErrLogMapper.listenEmailComputeErrLog(errLog);
            } finally {
                channel.basicAck(deliveryTag,false);
            }
            return;
        }

        // 收信人 每个收信箱 每小时 10 封
        Boolean hMax = redisTemplateLong.opsForValue().setIfAbsent(RedisKeyEnum.EMAIL_USER_HOUR_MAX.common(content.getRecipientEmail()), 0L, 1, TimeUnit.HOURS);
        if (hMax) {
            log.info("每个收信地址每小时发送10封信的锁已重置！");
        } else {
            Long hNum = redisTemplateLong.opsForValue().get(RedisKeyEnum.EMAIL_USER_HOUR_MAX.common(content.getRecipientEmail()));
            if (hNum >= 10L) {
                // 推迟投递的 放在 await中
                String randomed1 = RandomUtils.randomBetween(3612000L, 3600000L);
                rabbitMqUtils.convertAndSend(RabbitMqEnum.AWAIT_LETTER,dataString,message2 -> {
                    message2.getMessageProperties().setExpiration(randomed1);
                    return message2;
                });
                log.info("{}: 因每个收信地址发信数量超载而推后1h + {} 毫秒发送",emailId,randomed1);
                // 确认消费
                channel.basicAck(deliveryTag,false);
                return;
            }
        }


        // 发信！！！ 发信失败的逻辑，别忘了写
        try {
            qqEmailUtils.deliverySendHtml(content);
        } catch (Exception e) {
            ErrLog errLog1 = new ErrLog();
            errLog1.setId(UUID.randomUUID().toString().replace("-",""));
            errLog1.setEmailId(emailId);
            errLog1.setErrReason("收信人邮箱不存在？" + e.getMessage());
            emailErrLogMapper.listenEmailComputeErrLog(errLog1);
            channel.basicAck(deliveryTag,false);
            return;
        }



        emailContentMapper.updateisDeliverySucceed(emailId);

        // 每天信封 每秒信封 + 1
        redisTemplateLong.opsForValue().increment(RedisKeyEnum.EMAIL_DAY_MAX);
        redisTemplateLong.opsForValue().increment(RedisKeyEnum.EMAIL_S_MAX);
        redisTemplateLong.opsForValue().increment(RedisKeyEnum.EMAIL_USER_HOUR_MAX.common(content.getRecipientEmail()));
        channel.basicAck(deliveryTag,false);
    }


}
