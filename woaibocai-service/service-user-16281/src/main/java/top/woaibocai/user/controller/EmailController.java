package top.woaibocai.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import top.woaibocai.model.Enum.ResultCodeEnum;
import top.woaibocai.model.common.Result;
import top.woaibocai.model.dto.email.ContentDto;
import top.woaibocai.user.service.EmailService;
import top.woaibocai.user.utils.IpThrottlingUtils;

/**
 * @program: woaibocai-parent
 * @description: 关于时光邮局用户的业务
 * @author: LikeBocai
 * @create: 2024/7/18 13:21
 **/
@Tag(name = "时光邮局用户接口")
//未来如果某接口需要进行token的验证，那就这样写 /blog/user/auth/xxxx
@RequestMapping("/api/user/email")
@RestController
public class EmailController {

    @Resource
    private EmailService emailService;
    @Resource
    private IpThrottlingUtils ipThrottlingUtils;

    @Operation(summary = "用户-编写邮件")
    @PostMapping("writing")
    public Result emailWritingEmail(@RequestBody @Valid ContentDto content,@RequestHeader("x-forwarded-for") String ip) {
        Boolean exceed = ipThrottlingUtils.isExceed(ip);
        System.out.println(ip);
        if (exceed) return Result.build(null, ResultCodeEnum.IP_EXCEEDED);
        return emailService.emailWritingEmail(content,ip);
    }
}
