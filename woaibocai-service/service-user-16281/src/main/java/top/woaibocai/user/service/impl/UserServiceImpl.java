package top.woaibocai.user.service.impl;

import jakarta.annotation.Resource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import top.woaibocai.model.common.Result;
import top.woaibocai.model.dto.manager.UserLoginDto;
import top.woaibocai.user.service.UserService;

/**
 * @program: woaibocai-parent
 * @description: 处理用户信息校验
 * @author: woaibocai
 * @create: 2023-12-09 15:27
 **/
@Service
public class UserServiceImpl implements UserService {
    @Resource
    private RedisTemplate<String,String> redisTemplate;
    @Override
    public Result<String> login(UserLoginDto userLoginDto) {
        redisTemplate.opsForValue().set("test","test123");
        return null;
    }
}
