package top.woaibocai.user.service.impl;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import top.woaibocai.common.utils.MD5util;
import top.woaibocai.model.Do.user.UserLoginDo;
import top.woaibocai.model.common.Result;
import top.woaibocai.model.common.ResultCodeEnum;
import top.woaibocai.model.dto.manager.UserLoginDto;
import top.woaibocai.model.dto.manager.UserRegisterDto;
import top.woaibocai.model.dto.user.AuthorizationsDto;
import top.woaibocai.model.vo.LoginVo;
import top.woaibocai.model.vo.user.UserInfoVo;
import top.woaibocai.user.mapper.UserMapper;
import top.woaibocai.user.service.UserService;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @program: woaibocai-parent
 * @description: 处理用户信息校验
 * @author: woaibocai
 * @create: 2023-12-09 15:27
 **/
@Service
public class UserServiceImpl implements UserService {
    @Resource
    private HashOperations<String,String,String> hashOperationSSS;
    @Resource
    private RedisTemplate<String,String> redisTemplateString;
    @Resource
    private UserMapper userMapper;
    @Resource
    private ObjectMapper objectMapper;
    @Override
    public Result<String> login(UserLoginDto userLoginDto) {
        // 1.查user表
        UserLoginDo userData = userMapper.selectByUserName(userLoginDto.getUserName());
        // 没有用户 返回
        if (userData == null) {
            return Result.build(null, ResultCodeEnum.LOGIN_ERROR);
        }

        // 2.将dto密码用 MD5Util 解析后与数据库中的对比
        //不正确就返回
        if (!userData.getPassword().equals(MD5util.onlySaltPass(userLoginDto.getPassword()))) {
            return Result.build(null, ResultCodeEnum.LOGIN_ERROR);
        }
        // 检查 status 如果值为 1 则为 封禁状态
        if (userData.getStatus().equals("1")) {
            return Result.build(null,ResultCodeEnum.ACCOUNT_STOP);
        }

        // 3.查询redis 中 refresh_token 是否 过期  没过期删除
        Map<String, String> refreshToken = hashOperationSSS.entries("user:refresh_token:" + userData.getRefreshToken());
        if (!refreshToken.isEmpty()) {
            redisTemplateString.delete("user:refresh_token:" + userData.getRefreshToken());
        }
        // 把userinfo 放进redis里
        UserInfoVo userInfoVo = userMapper.userInfo(userData.getId());
        Map map = objectMapper.convertValue(userInfoVo, Map.class);
        System.out.println(userInfoVo);
        // 4.创建 token 和 refresh_token 放进redis中
        String token = UUID.randomUUID().toString().replace("-", "");
        String refresh_token = UUID.randomUUID().toString().replace("-", "");
        hashOperationSSS.putAll("user:token:" + token,map);
        redisTemplateString.expire("user:token:" + token,10,TimeUnit.MINUTES);
        hashOperationSSS.putAll("user:refresh_token:" + refresh_token,map);
        redisTemplateString.expire("user:refresh_token:" + refresh_token,15, TimeUnit.DAYS);
        // 5. 把新 refresh_token 放进数据库 里
        userMapper.updateRefreshTokenById(userData.getId(),refresh_token);

        // 6. 把两个token 放进 loginVo 并 转成JSON字符串 返回
        LoginVo loginVo = new LoginVo();
        loginVo.setToken(token);
        loginVo.setRefresh_token(refresh_token);
        String loginVoJson = JSON.toJSONString(loginVo);

        return Result.build(loginVoJson,ResultCodeEnum.SUCCESS);
    }

    @Override
    public Result register(UserRegisterDto userRegisterDto) {
        // 1.先查数据库中 userName 是否相等
        Integer userNameNum = userMapper.userNameValidate(userRegisterDto.getUserName());
        if (userNameNum != 0) {
            return Result.build(null,ResultCodeEnum.USER_NAME_IS_EXISTS);
        }
        // 2.再查数据库中邮箱是否重复
        Integer emailNum = userMapper.emailValidate(userRegisterDto.getEmail());
        if (emailNum != 0) {
            return Result.build(null,ResultCodeEnum.EMAIL_IS_EXISTS);
        }
        // 3.把密码加密 整合插入
        String md5Pwd = MD5util.onlySaltPass(userRegisterDto.getPassword());
        userRegisterDto.setPassword(md5Pwd);
        String id = UUID.randomUUID().toString().replace("-", "");
        Boolean flag = userMapper.register(userRegisterDto,id);
        if (flag.equals(false)) {
            return Result.build(null,ResultCodeEnum.DATA_ERROR);
        }
        return Result.build(null,ResultCodeEnum.SUCCESS);
    }

    @Override
    public Result<String> authorizations(AuthorizationsDto authorizationsDto) {
        // 去 redis 里查询 refresh_token 是否过期
        Map<String, String> refreshTokenUserInfo = hashOperationSSS.entries("user:refresh_token:" + authorizationsDto.getRefresh_token());
        if (refreshTokenUserInfo.isEmpty()) {
            return Result.build(null,ResultCodeEnum.LOGIN_AUTH);
        }
        // 创建 token
        String token = UUID.randomUUID().toString().replace("-", "");
        // 放入redis
        hashOperationSSS.putAll("user:token:" + token,refreshTokenUserInfo);
        redisTemplateString.expire("user:token:" + token,10,TimeUnit.MINUTES);
        // 转化json字符串
        LoginVo loginVo = new LoginVo();
        loginVo.setToken(token);
        loginVo.setRefresh_token(authorizationsDto.getRefresh_token());
        String loginVoToString = JSON.toJSONString(loginVo);
        return Result.build(loginVoToString,ResultCodeEnum.SUCCESS);
    }

    @Override
    public Result getUserInfo(String newToken) {
        Map<String, String> userInfo = hashOperationSSS.entries("user:token:" + newToken);
        if (userInfo.isEmpty()) {
            return Result.build(null,ResultCodeEnum.LOGIN_NOLL);
        }
        UserInfoVo userInfoVo = objectMapper.convertValue(userInfo, UserInfoVo.class);
//        UserInfoVo userInfoVo = JSON.parseObject(userInfo, UserInfoVo.class);
        return Result.build(JSON.toJSONString(userInfoVo),ResultCodeEnum.SUCCESS);
    }

    @Override
    public Result logout(LoginVo loginVo) {
        //先删除 refresh_token
        Long size = hashOperationSSS.size("user:refresh_token:" + loginVo.getRefresh_token());
        if (size != 0L) {
//            redisTemplateString.delete("user:refresh_token:" + loginVo.getRefresh_token());
            redisTemplateString.delete("user:refresh_token:" + loginVo.getRefresh_token());
        }
        //删除 token
//        String token = redisTemplate.opsForValue().get("user:token:" + loginVo.getToken());
        redisTemplateString.delete("user:token:" + loginVo.getToken());
        return Result.build(null,ResultCodeEnum.SUCCESS);
    }
}
