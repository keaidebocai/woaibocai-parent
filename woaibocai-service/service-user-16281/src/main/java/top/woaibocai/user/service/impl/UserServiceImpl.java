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
import top.woaibocai.model.common.RedisKeyEnum;
import top.woaibocai.model.common.Result;
import top.woaibocai.model.common.ResultCodeEnum;
import top.woaibocai.model.common.User;
import top.woaibocai.model.dto.manager.UserLoginDto;
import top.woaibocai.model.dto.manager.UserRegisterDto;
import top.woaibocai.model.dto.user.AuthorizationsDto;
import top.woaibocai.model.dto.user.UserForgotDto;
import top.woaibocai.model.vo.LoginVo;
import top.woaibocai.model.vo.user.UserInfoVo;
import top.woaibocai.user.mapper.UserMapper;
import top.woaibocai.user.service.UserService;
import top.woaibocai.user.utils.QQEmailUtils;

import java.util.Map;
import java.util.Objects;
import java.util.Random;
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
    private RedisTemplate<String,Integer> redisTemplateObject;
    @Resource
    private UserMapper userMapper;
    @Resource
    private QQEmailUtils qqEmailUtils;
    @Resource
    private ObjectMapper objectMapper;
    @Override
    public Result<String> login(UserLoginDto userLoginDto) {
        // 用redis查
        Integer count = redisTemplateObject.opsForValue().get(RedisKeyEnum.BLOG_LOGIN_COUNT.common(userLoginDto.getUserName()));
        if (count == null) {
            redisTemplateObject.opsForValue().set(RedisKeyEnum.BLOG_LOGIN_COUNT.common(userLoginDto.getUserName()),1,10,TimeUnit.MINUTES);
        } else if (count >= 5){
            return Result.build(null,204,"该账号短时间内错误登录过多，封禁10分钟!");
        }
        // 1.查user表
        UserLoginDo userData = userMapper.selectByUserName(userLoginDto.getUserName());
        // 没有用户 返回
        if (userData == null) {
            return Result.build(null, ResultCodeEnum.LOGIN_ERROR);
        }

        // 2.将dto密码用 MD5Util 解析后与数据库中的对比
        //不正确就返回
        if (!userData.getPassword().equals(MD5util.onlySaltPass(userLoginDto.getPassword()))) {
            redisTemplateObject.opsForValue().increment(RedisKeyEnum.BLOG_LOGIN_COUNT.common(userLoginDto.getUserName()),1);
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
        // 4.创建 token 和 refresh_token 放进redis中
        String token = UUID.randomUUID().toString().replace("-", "");
        String refresh_token = UUID.randomUUID().toString().replace("-", "");
        hashOperationSSS.putAll("user:token:" + token,map);
        redisTemplateString.expire("user:token:" + token,10,TimeUnit.HOURS);
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
        String code = redisTemplateString.opsForValue().get(RedisKeyEnum.BLOG_REGISTER_EMAIL.common(userRegisterDto.getEmail()));
        if (code == null) {
            return Result.build(null,204,"验证码过期!");
        }
        if (!code.equals(userRegisterDto.getCode())) {
            return Result.build(null,204,"验证码错误!");
        }
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
        // 昵称不能重复
        Integer nickNameValidate = userMapper.nickNameValidate(userRegisterDto.getNickName());
        if (nickNameValidate != 0) {
            return Result.build(null,204,"你和别人想到了同一个昵称诶!");
        }
        // 3.把密码加密 整合插入
        String md5Pwd = MD5util.onlySaltPass(userRegisterDto.getPassword());
        userRegisterDto.setPassword(md5Pwd);
        String id = UUID.randomUUID().toString().replace("-", "");
        Boolean flag = userMapper.register(userRegisterDto,id);
        if (flag.equals(false)) {
            return Result.build(null,ResultCodeEnum.DATA_ERROR);
        }
        redisTemplateString.delete(RedisKeyEnum.BLOG_REGISTER_EMAIL.common(userRegisterDto.getEmail()));
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
        redisTemplateString.expire("user:token:" + token,10,TimeUnit.HOURS);
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

    @Override
    public Result registerEmail(String email) {
        // 先查询 数据库中是否有相同的邮箱
        Integer isEmail = userMapper.emailValidate(email);
        if (isEmail > 0) {
            return Result.build(null,204,"此邮箱已注册过！");
        }
        // 检查redis中是否有此邮箱
        if (!Objects.requireNonNull(redisTemplateString.opsForValue().get(RedisKeyEnum.BLOG_REGISTER_EMAIL.common(email))).isEmpty()) {
            return Result.build(null,200,"此邮箱的验证码尚未过期！");
        }
        // 生成 6位数字验证码
        Random random = new Random();
        String code = String.valueOf(random.nextInt(900000) + 100000);
        // 发送邮件 如果失败了就抛出 并返回失败
        String html = "<h1>注册验证码:"+ code +"</h1><img style='width: 360px;height:360px' src='https://cdn.likebocai.com/bcblog/public/src/avatar-3.jpg'/><br><p>验证码5分钟有效.</p>";
        Boolean title = qqEmailUtils.sendHtml("【菠菜的小窝】欢迎加入菠菜的小窝!", html, email);
        if (!title) {
            return Result.build(null,204,"邮件服务器出现异常请稍后再试！");
        }
        // 推上 redis 过期时间设置为5分钟
        redisTemplateString.opsForValue().set(RedisKeyEnum.BLOG_REGISTER_EMAIL.common(email), code,5,TimeUnit.MINUTES);
        return Result.build(null,200,"请及时查看邮箱!");
    }

    @Override
    public Result forgot(UserForgotDto userForgotDto) {
        // code
        String code = redisTemplateString.opsForValue().get(RedisKeyEnum.BLOG_FORGOT_EMAIL.common(userForgotDto.getEmail()));
        if (code == null) {
            return Result.build(null,204,"验证码过期!");
        }
        if (!code.equals(userForgotDto.getCode())) {
            return Result.build(null,204,"验证码错误!");
        }
        // 根据 userName 查询 id和邮箱
        User user = userMapper.selectIdEmailByUserName(userForgotDto.getUserName());
        if (user == null) {
            return Result.build(null,204,"用户不存在");
        }
        if (!user.getEmail().equals(userForgotDto.getEmail())) {
            return Result.build(null,204,"这不是你的邮箱!你邮箱呢?");
        }
        // 3.把密码加密 整合插入
        String md5Pwd = MD5util.onlySaltPass(userForgotDto.getPassword());
        userForgotDto.setPassword(md5Pwd);
        // 修改密码 懒了不想写返回影响行数了，摆！
        userMapper.updatePasswordById(user.getId(),userForgotDto.getPassword());
        redisTemplateObject.delete(RedisKeyEnum.BLOG_FORGOT_EMAIL.common(userForgotDto.getEmail()));
        return Result.build(null,ResultCodeEnum.SUCCESS);
    }

    @Override
    public Result forgotEmail(String email,String userName) {
        // 检查redis中是否有此邮箱
        if (!Objects.requireNonNull(redisTemplateString.opsForValue().get(RedisKeyEnum.BLOG_FORGOT_EMAIL.common(email))).isEmpty()) {
            return Result.build(null,200,"此邮箱的验证码尚未过期！");
        }
        // 根据邮箱 查 用户名
        String name = userMapper.selectUserNameByEmail(email);
        if (name == null) {
            return Result.build(null,204,"该邮箱尚未注册");
        }
        if (!name.equals(userName)) {
            return Result.build(null,204,"请查看邮箱/用户名是否匹配。");
        }
        // 生成 6位数字验证码
        Random random = new Random();
        String code = String.valueOf(random.nextInt(900000) + 100000);
        // 发送邮件 如果失败了就抛出 并返回失败
        String html = "<h1>验证码:"+ code +"</h1><br><p>验证码5分钟有效.</p>";
        Boolean title = qqEmailUtils.sendHtml("【菠菜的小窝】忘记密码", html, email);
        if (!title) {
            return Result.build(null,204,"邮件服务器出现异常请稍后再试！");
        }
        // 推上 redis 过期时间设置为5分钟
        redisTemplateString.opsForValue().set(RedisKeyEnum.BLOG_FORGOT_EMAIL.common(email), code,5,TimeUnit.MINUTES);
        return Result.build(null,200,"请及时查看邮箱!");
    }

    @Override
    public Result updateUserInfo(UserInfoVo userInfoVo) {
        // 查询 昵称是否重复
        Integer integer = userMapper.nickNameValidate(userInfoVo.getNickName());
        if (integer > 1) {
            return Result.build(null,204,"昵称重复!");
        }
        userMapper.updateUserInfo(userInfoVo);
        return Result.build(null,ResultCodeEnum.SUCCESS);
    }
}
