package top.woaibocai.manager.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.druid.util.StringUtils;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import top.woaibocai.common.exception.HttpException;
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
    public Result<String> login(UserLoginDto userLoginDTO) {
        //1.先根据userName查询数据库
        User user = userMapper.selectByUserName(userLoginDTO.getUserName());
        //如果有，那就对比密码是否正确
        if (user == null){
            return Result.build(null,ResultCodeEnum.LOGIN_ERROR);
//            throw new BoCaiException(ResultCodeEnum.LOGIN_ERROR);
        }
        //密码不一致那就返回登陆失败
        if (!user.getPassword().equals(userLoginDTO.getPassword())){
            return Result.build(null,ResultCodeEnum.LOGIN_ERROR);
            //            throw new BoCaiException(ResultCodeEnum.LOGIN_ERROR);
        }
        //拿出数据库中refresh_token，然后再redis中查询有没有如果有的话，就把他删了，保持refresh_token唯一性
        String historyRefreshToken = redisTemplate.opsForValue().get("user::refresh_token:" + user.getRefreshToken());
        if (!StringUtils.isEmpty(historyRefreshToken)){
            redisTemplate.delete("user::refresh_token:" + user.getRefreshToken());
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
        //把最新的 refresh_token 插入到数据库里
        userMapper.updateOfRefreshToken(refresh_token,user.getId());
        String loginToString = JSON.toJSONString(loginVo);
        return Result.build(loginToString,ResultCodeEnum.SUCCESS);
    }

    @Override
    public Result getUserInfo(String token) {
        //先根据token查寻，如果为空那就返回过期重新登录，否则返回
        String userInfoJson = redisTemplate.opsForValue().get("user::token:" + token);
        if (StringUtils.isEmpty(userInfoJson)){
//            throw new BoCaiException(ResultCodeEnum.LOGIN_NOLL);
//            return Result.build(null,ResultCodeEnum.LOGIN_NOLL);
            throw new HttpException();
        }
        User userInfo = JSON.parseObject(userInfoJson, User.class);
        userInfo.setPassword("你难道没有自己的密码吗？");
        return Result.build(userInfo,ResultCodeEnum.SUCCESS);
    }
    //用refresh_token更新失效的token
    @Override
    public Result<String> authorizations(String refresh_token) {
        String userInfoByRefresh_tokenJson = redisTemplate.opsForValue().get("user::refresh_token:" + refresh_token);
        if (StringUtils.isEmpty(userInfoByRefresh_tokenJson)){
            return Result.build(null,ResultCodeEnum.LOGIN_AUTH);
        }
        //refresh_token再redis里的value是user.getUserName()
        //根据user.getUserName()查询数据库，封装好信息们惶恐redis里
        User user = userMapper.selectByUserName(userInfoByRefresh_tokenJson);
        String token = UUID.randomUUID().toString().replace("-", "");
        redisTemplate
                .opsForValue()
                .set("user::token:" + token, JSON.toJSONString(user),1, TimeUnit.MINUTES);
        LoginVo loginVo = new LoginVo();
        loginVo.setToken(token);
        loginVo.setRefresh_token(refresh_token);
        String loginToString = JSON.toJSONString(loginVo);
        return Result.build(loginToString,ResultCodeEnum.SUCCESS);
    }

    @Override
    public Result logout(String newToken,LoginVo loginVo) {
        //建议前端把两个token都穿过来，或者只传refresh_token
        //1.验证请求头里的token是否合法
        String newTokenRedis = redisTemplate.opsForValue().get("user::token:" + newToken);
        if (StringUtils.isEmpty(newTokenRedis) || !newToken.equals(loginVo.getToken())){
            throw new HttpException();
        }
        //2.分别在redis里查询两条token是否存在，可以不查token
        //查token //3.删除redis里的两条token
        String refresh_tokenByRedis = redisTemplate.opsForValue().get("user::refresh_token:" + loginVo.getRefresh_token());
        if (!StringUtils.isEmpty(refresh_tokenByRedis)) {
            redisTemplate.delete("user::refresh_token:" + loginVo.getRefresh_token());
        }
        redisTemplate.delete("user::token:" + newToken);
        System.out.println("succes!");
        return Result.build(null,ResultCodeEnum.SUCCESS);
    }
}
