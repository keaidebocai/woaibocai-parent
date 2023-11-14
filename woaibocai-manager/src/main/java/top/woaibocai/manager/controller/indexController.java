package top.woaibocai.manager.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import top.woaibocai.manager.service.UserService;
import top.woaibocai.model.common.Result;
import top.woaibocai.model.dto.UserLoginDto;
import top.woaibocai.model.vo.LoginVo;

/**
 * @program: woaibocai-parent
 * @description: 登录相关
 * @author: woaibocai
 * @create: 2023-11-07 11:50
 **/
@Tag(name = "登录注册")
@RestController
@RequestMapping("admin/api/manager")
public class indexController {
    @Resource
    private UserService userService;
    @Operation(summary = "登录")
    @PostMapping("login")
    public Result<String> login(@RequestBody UserLoginDto userLoginDTO){
        return userService.login(userLoginDTO);
    }
    @Operation(summary = "获取用户信息")
    @GetMapping("userInfo")
    public Result getUserInfo(@RequestHeader(name = "Authorization") String token){
        System.out.println("token = " + token);
        String newToken = token.replace("Bearer ", "");
        return userService.getUserInfo(newToken);
    }

    @Operation(summary = "用refresh_token更新失效的token")
    @PostMapping("authorizations")
    public Result<String> authorizations(String refresh_token){
        return userService.authorizations(refresh_token);
    }

    @Operation(summary = "退出")
    @PostMapping("logout")
    public Result logout(@RequestHeader(name = "Authorization") String token,
                         @RequestBody LoginVo loginVo){
        String newToken = token.replace("Bearer ", "");
        return userService.logout(newToken,loginVo);
    }

}
