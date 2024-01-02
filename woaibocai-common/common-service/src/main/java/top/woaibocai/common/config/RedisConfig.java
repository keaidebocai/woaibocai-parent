package top.woaibocai.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import top.woaibocai.model.entity.blog.Menu;

/**
 * @program: woaibocai-parent
 * @description:
 * * *redis序列化的工具定置类，下面这个请一定开启配置
 * *127.0.0.1:6379> keys *
 * *1) “ord:102” 序列化过
 * *2)“\xaclxedlxeelx05tixeelaord:102” 野生，没有序列化过
 * *this.redisTemplate.opsForValue(); //提供了操作string类型的所有方法
 * *this.redisTemplate.opsForList();// 提供了操作List类型的所有方法
 * *this.redisTemplate.opsForset(); //提供了操作set类型的所有方法
 * *this.redisTemplate.opsForHash(); //提供了操作hash类型的所有方认
 * *this.redisTemplate.opsForZSet(); //提供了操作zset类型的所有方法
 * @author: woaibocai
 * @create: 2023-12-09 15:42
 **/
@Configuration
public class RedisConfig {
    // 如果未来 时间数据 出问题了， 记得改这个
    @Bean
    public RedisTemplate<String,Object> redisTemplateObject(LettuceConnectionFactory lettuceConnectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(lettuceConnectionFactory);

        //设置key序列化方式String
        // RedisSerializer.string() 等价于 new StringRedisSerializer()
        redisTemplate.setKeySerializer(RedisSerializer.string());

        // 设置value的序列化方式json，使用GenericJackson2JsonRedisSerializer替换默认序列化
        // RedisSerializer.json() 等价于 new GenericJackson2JsonRedisSerializer()
        redisTemplate.setValueSerializer(RedisSerializer.json());

        // 设置hash的key的序列化方式
        redisTemplate.setHashKeySerializer(RedisSerializer.string());

        // 设置hash的value的序列化方式
        redisTemplate.setHashValueSerializer(RedisSerializer.json());
        // 使配置生效
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }
    @Bean
    public HashOperations<String,String,Object> hashOperationSSO(RedisTemplate<String,Object> redisTemplateObject) {
        return redisTemplateObject.opsForHash();
    }
    @Bean
    public RedisTemplate<String,String> redisTemplateString(LettuceConnectionFactory lettuceConnectionFactory) {
        RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(lettuceConnectionFactory);

        //设置key序列化方式String
        // RedisSerializer.string() 等价于 new StringRedisSerializer()
        redisTemplate.setKeySerializer(RedisSerializer.string());

        // 设置value的序列化方式json，使用GenericJackson2JsonRedisSerializer替换默认序列化
        // RedisSerializer.json() 等价于 new GenericJackson2JsonRedisSerializer()
        redisTemplate.setValueSerializer(RedisSerializer.json());

        // 设置hash的key的序列化方式
        redisTemplate.setHashKeySerializer(RedisSerializer.string());

        // 设置hash的value的序列化方式
        redisTemplate.setHashValueSerializer(RedisSerializer.json());
        // 使配置生效
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }
    @Bean
    public HashOperations<String,String,String> hashOperationSSS(RedisTemplate<String,String> redisTemplateString) {
        return redisTemplateString.opsForHash();
    }
    @Bean
    public ListOperations<String,String> listOperationSS(RedisTemplate<String,String> redisTemplateString) {
        return redisTemplateString.opsForList();
    }
    @Bean
    public RedisTemplate<String,Integer> redisTemplateInteger(LettuceConnectionFactory lettuceConnectionFactory) {
        RedisTemplate<String, Integer> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(lettuceConnectionFactory);

        //设置key序列化方式String
        // RedisSerializer.string() 等价于 new StringRedisSerializer()
        redisTemplate.setKeySerializer(RedisSerializer.string());

        // 设置value的序列化方式json，使用GenericJackson2JsonRedisSerializer替换默认序列化
        // RedisSerializer.json() 等价于 new GenericJackson2JsonRedisSerializer()
        redisTemplate.setValueSerializer(new GenericToStringSerializer<>(Integer.class));

        // 设置hash的key的序列化方式
        redisTemplate.setHashKeySerializer(RedisSerializer.string());

        // 设置hash的value的序列化方式
        redisTemplate.setHashValueSerializer(RedisSerializer.json());
        // 使配置生效
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }
    @Bean
    public HashOperations<String,String,Integer> hashOperationSSI(RedisTemplate<String,Integer> redisTemplateInteger) {
        return redisTemplateInteger.opsForHash();
    }

}
