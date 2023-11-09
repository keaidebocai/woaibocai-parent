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
    public Result<LoginVo> login(@RequestBody UserLoginDto userLoginDTO){
        LoginVo loginVo = userService.login(userLoginDTO);
        return Result.build(loginVo,200,"操作成功！");
    }
    @Operation(summary = "获取用户信息")
    @GetMapping("userInfo")
    public Result getUserInfo(@RequestHeader(name = "Authorization") String token){
        String newToken = token.replace("Bearer ", "");
        return userService.getUserInfo(newToken);
    }

    @Operation(summary = "用refresh_token更新失效的token")
    @PutMapping("authorizations")
    public Result<LoginVo> authorizations(@RequestHeader(name = "refresh_token") String token){
        String newToken = token.replace("Bearer ", "");
        LoginVo loginVo = userService.authorizations(newToken);
        return Result.build(loginVo,200,"操作成功！");
    }


}
