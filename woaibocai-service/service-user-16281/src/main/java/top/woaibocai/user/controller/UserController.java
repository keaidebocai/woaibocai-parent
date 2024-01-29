package top.woaibocai.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import top.woaibocai.model.common.Result;
import top.woaibocai.model.common.ResultCodeEnum;
import top.woaibocai.model.dto.manager.UserLoginDto;
import top.woaibocai.model.dto.manager.UserRegisterDto;
import top.woaibocai.model.dto.user.AuthorizationsDto;
import top.woaibocai.model.vo.LoginVo;
import top.woaibocai.user.service.UserService;
import top.woaibocai.user.utils.QQEmailUtils;


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
    private QQEmailUtils qqEmailUtils;
    @Operation(summary = "用户登录")
    @PostMapping("login")
    public Result<String> login(@RequestBody UserLoginDto userLoginDto) {
        return userService.login(userLoginDto);
    }
    @Operation(summary = "注册demo")
    @PostMapping("register")
    public Result register(@RequestBody UserRegisterDto userRegisterDto) {
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
        System.out.println(newToken);
        return userService.getUserInfo(newToken);
    }
    @Operation(summary = "退出账号")
    @PostMapping("logout")
    public Result logout(@RequestBody LoginVo loginVo){
        return userService.logout(loginVo);
    }
    @Operation(summary = "邮箱验证测试")
    @GetMapping("email/{emial}")
    public Result email(@PathVariable String emial) {
        String html = "<h1>注册验证码:114514</h1><img style='width: 360px;height:360px' src='https://cdn.likebocai.com/bcblog/public/src/avatar-3.jpg'/><br><p>验证码5分钟有效.</p>";
        Boolean title = qqEmailUtils.sendHtml("【菠菜的小窝】欢迎加入菠菜的小窝!", html, emial);
        return Result.build(title, ResultCodeEnum.SUCCESS);
    }
}
