package top.woaibocai.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.woaibocai.model.common.Result;
import top.woaibocai.model.dto.manager.UserLoginDto;
import top.woaibocai.user.service.UserService;

/**
 * @program: woaibocai-parent
 * @description: 处理登录等...接口
 * @author: woaibocai
 * @create: 2023-12-09 15:18
 **/
@Tag(name = "用户信息验证")
//未来如果某接口需要进行token的验证，那就这样写 /blog/user/auth/xxxx
@RequestMapping("/blog/user")
@RestController
public class UserController {
    @Resource
    private UserService userService;
    @Operation(summary = "用户登录")
    @PostMapping("login")
    public Result<String> login(@RequestBody UserLoginDto userLoginDto) {
        return userService.login(userLoginDto);
    }
}
