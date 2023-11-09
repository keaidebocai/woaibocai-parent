package top.woaibocai.manager.service.impl;

import com.alibaba.fastjson.JSON;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import top.woaibocai.common.exception.BoCaiException;
import top.woaibocai.manager.mapper.UserMapper;
import top.woaibocai.manager.service.UserService;
import top.woaibocai.model.common.Result;
import top.woaibocai.model.common.ResultCodeEnum;
import top.woaibocai.model.common.User;
import top.woaibocai.model.dto.UserLoginDto;
import top.woaibocai.model.vo.LoginVo;

import java.util.UUID;
import java.util.concurrent.TimeUnit;


/**
 * @program: woaibocai-parent
 * @description:
 * @author: woaibocai
 * @create: 2023-11-07 12:20
 **/
@Service
public class UserServiceImpl implements UserService {
    @Resource
    RedisTemplate<String,String> redisTemplate;
    @Resource
    private UserMapper userMapper;
    @Override
    public LoginVo login(UserLoginDto userLoginDTO) {
        //1.先根据userName查询数据库
        User user = userMapper.selectByUserName(userLoginDTO.getUserName());
        //如果有，那就对比密码是否正确
        if (user == null){
            throw new BoCaiException(ResultCodeEnum.LOGIN_ERROR);
        }
        //密码不一致那就返回登陆失败
        if (!user.getPassword().equals(userLoginDTO.getPassword())){
            throw new BoCaiException(ResultCodeEnum.LOGIN_ERROR);
        }
        //密码一致，创建token并封装loginVo返回
        LoginVo loginVo = new LoginVo();
        String token = UUID.randomUUID().toString().replace("-", "");
        String refresh_token = UUID.randomUUID().toString().replace("-", "");
        redisTemplate
                .opsForValue()
                .set("user::token:" + token, JSON.toJSONString(user),1, TimeUnit.MINUTES);
        redisTemplate
                .opsForValue()
                        .set("user::refresh_token:" + refresh_token ,user.getUserName(),7,TimeUnit.DAYS);
        loginVo.setToken(token);
        loginVo.setRefresh_token(refresh_token);
        return loginVo;
    }

    @Override
    public Result getUserInfo(String token) {
        //先根据token查寻，如果为空那就返回过期重新登录，否则返回
        String userInfoJson = redisTemplate.opsForValue().get("user::token:" + token);
        if (!StringUtils.hasText(userInfoJson)){
//            throw new BoCaiException(ResultCodeEnum.LOGIN_AUTH);
            return Result.build(null,ResultCodeEnum.LOGIN_AUTH);
        }
        User userInfo = JSON.parseObject(userInfoJson, User.class);
        return Result.build(userInfo,ResultCodeEnum.SUCCESS);
    }
    //用refresh_token更新失效的token
    @Override
    public LoginVo authorizations(String refresh_token) {
        String userInfoByRefresh_tokenJson = redisTemplate.opsForValue().get("user::refresh_token:" + refresh_token);
        if (!StringUtils.hasText(userInfoByRefresh_tokenJson)){
            throw new BoCaiException(ResultCodeEnum.LOGIN_AUTH);
        }
        String token = UUID.randomUUID().toString().replace("-", "");
        redisTemplate
                .opsForValue()
                .set("user::token:" + token, userInfoByRefresh_tokenJson,1, TimeUnit.MINUTES);
        LoginVo loginVo = new LoginVo();
        loginVo.setToken(token);
        System.out.println("token = " + token);
        loginVo.setRefresh_token(refresh_token);
        System.out.println("refresh_token = " + refresh_token);
        return loginVo;
    }
}
