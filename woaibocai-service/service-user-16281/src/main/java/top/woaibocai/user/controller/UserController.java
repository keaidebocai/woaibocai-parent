package top.woaibocai.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import top.woaibocai.common.feign.QCloudFeignClint;
import top.woaibocai.model.common.Result;
import top.woaibocai.model.common.ResultCodeEnum;
import top.woaibocai.model.dto.manager.UserLoginDto;
import top.woaibocai.model.dto.manager.UserRegisterDto;
import top.woaibocai.model.dto.user.AuthorizationsDto;
import top.woaibocai.model.dto.user.UserForgotDto;
import top.woaibocai.model.vo.LoginVo;
import top.woaibocai.model.vo.user.UserInfoVo;
import top.woaibocai.user.service.UserService;


import java.io.IOException;


/**
 * @program: woaibocai-parent
 * @description: 处理登录等...接口
 * @author: woaibocai
 * @create: 2023-12-09 15:18
 **/
@Tag(name = "用户信息验证")
//未来如果某接口需要进行token的验证，那就这样写 /blog/user/auth/xxxx
@RequestMapping("/api/user")
@RestController
public class UserController {
    @Resource
    private UserService userService;
    @Resource
    private QCloudFeignClint qCloudFeignClint;
    @Operation(summary = "用户登录")
    @PostMapping("login")
    public Result<String> login(@RequestBody UserLoginDto userLoginDto) {
        return userService.login(userLoginDto);
    }
    @Operation(summary = "注册demo")
    @PostMapping("register")
    public Result register(@RequestBody UserRegisterDto userRegisterDto) {
        if (userRegisterDto.getUserName().isEmpty() | userRegisterDto.getPassword().isEmpty() | userRegisterDto.getPassword().isEmpty() | userRegisterDto.getNickName().isEmpty() | userRegisterDto.getEmail().isEmpty() | userRegisterDto.getCode().isEmpty()){
            return Result.build(null,204,"看看什么没写吧!");
        }
        if (!userRegisterDto.getEmail().matches("^[a-zA-Z0-9._%+-]+@(qq\\.com|foxmail\\.com|gmail\\.com|163\\.com|aliyun\\.com)$")) {
            return Result.build(null,204,"邮箱格式不正确!");
        }
        return userService.register(userRegisterDto);
    }
    @Operation(summary = "刷新token")
    @PostMapping("authorizations")
    public Result<String> authorizations(@RequestBody AuthorizationsDto authorizationsDto){
        return userService.authorizations(authorizationsDto);
    }
    @Operation(summary = "获取用户信息")
    @GetMapping("auth/getUserInfo")
    public Result getUserInfo(@RequestHeader("Authorization") String token){
        String newToken = token.replace("Bearer ", "");
        return userService.getUserInfo(newToken);
    }
    @Operation(summary = "退出账号")
    @PostMapping("auth/logout")
    public Result logout(@RequestBody LoginVo loginVo){
        return userService.logout(loginVo);
    }
    @Operation(summary = "邮箱验证码")
    @GetMapping("registerEmail/{email}")
    public Result email(@PathVariable String email) {
        if (!email.matches("^[a-zA-Z0-9._%+-]+@(qq\\.com|foxmail\\.com|gmail\\.com|163\\.com|aliyun\\.com)$")) {
            return Result.build(null,204,"邮箱格式不支持!");
        }
        return userService.registerEmail(email);
    }
    @Operation(summary = "忘记密码")
    @PostMapping("forgot")
    public Result forgot(@RequestBody UserForgotDto userForgotDto) {
        if (userForgotDto.getUserName().isEmpty() | userForgotDto.getPassword().isEmpty() | userForgotDto.getEmail().isEmpty() | userForgotDto.getCode().isEmpty()){
            return Result.build(null,204,"看看什么没写吧!");
        }
        if (!userForgotDto.getEmail().matches("^[a-zA-Z0-9._%+-]+@(qq\\.com|foxmail\\.com|gmail\\.com|163\\.com|aliyun\\.com)$")) {
            return Result.build(null,204,"邮箱格式不正确!");
        }
        return userService.forgot(userForgotDto);
    }
    @Operation(summary = "忘记密码的邮箱验证")
    @GetMapping("forgotEmail/{email}/{userName}")
    public Result forgotEmail(@PathVariable String email,@PathVariable String userName) {
        if (!email.matches("^[a-zA-Z0-9._%+-]+@(qq\\.com|foxmail\\.com|gmail\\.com|163\\.com|aliyun\\.com)$")) {
            return Result.build(null,204,"邮箱格式不支持!");
        }
        if (userName.isEmpty()) {
            return Result.build(null,204,"用户名不能为空!");
        }
        return userService.forgotEmail(email,userName);
    }
    @Operation(summary = "用户上传头像")
    @PostMapping("auth/userLoadAvatar")
    public Result<String> userLoadAvatar(MultipartFile file) throws IOException {
        Result result = qCloudFeignClint.userUploadAvatar(file);
        return Result.build(result.getData(),ResultCodeEnum.SUCCESS);
    }
    @Operation(summary = "修改用户信息")
    @PostMapping("auth/updateUserInfo")
    public Result updateUserInfo(@RequestBody UserInfoVo userInfoVo) {
        if (userInfoVo.getUserId().isEmpty() | userInfoVo.getNickName().isEmpty() | userInfoVo.getSex().isEmpty() | userInfoVo.getAvatar().isEmpty()) {
            return Result.build(null,204,"看看你少些什么东西啦!");
        }
        return userService.updateUserInfo(userInfoVo);
    }
}
