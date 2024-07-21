package top.woaibocai.user.utils;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @program: woaibocai-parent
 * @description: 根据ip限流
 * @author: LikeBocai
 * @create: 2024/7/20 11:04
 **/
@Component
@Slf4j
public class IpThrottlingUtils {
    @Resource
    private RedisTemplate<String,Long> redisTemplateLong;

    /**
     * @author: LikeBocai
     * @description: true:超过了，flase: 没超过
     * @param: [ip]
     * @return: java.lang.Boolean
    **/
    public Boolean isExceed(String ip) {
        // 当 key 不存在时 给他设置一个值 不存在返回 true 存在返回 false
        Boolean isAb = redisTemplateLong.opsForValue().setIfAbsent(ip, 0L, 1, TimeUnit.DAYS);
        return isAb ? false : (Long.valueOf(redisTemplateLong.opsForValue().get(ip)) >= 5L ? true : false);
    }

    public void increment(String ip){
        // 如果 key 不存在 那么 那会先初始化成0 然后再 increment 操作
        redisTemplateLong.opsForValue().increment(ip, 1L);
    }

}
