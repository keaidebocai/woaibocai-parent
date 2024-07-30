package top.woaibocai.blog.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Positive;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.woaibocai.blog.service.EmailService;
import top.woaibocai.model.Enum.ResultCodeEnum;
import top.woaibocai.model.common.Result;
import top.woaibocai.model.vo.email.EmailPublicVo;
import top.woaibocai.model.vo.email.LikebocaiPageVo;

/**
 * @program: woaibocai-parent
 * @description: 时光邮局相关业务
 * @author: LikeBocai
 * @create: 2024/7/21 11:56
 **/
@Tag(name = "时光邮局业务")
@RestController
@RequestMapping("/api/blog/email")
public class EmailController {

    @Resource
    private EmailService emailService;

    @Operation(summary = "公开信-分页")
    @GetMapping("public/{type}/{current}/{size}")
    public Result<LikebocaiPageVo<EmailPublicVo>> publicEmailList(@PathVariable String type,
                                                                  @PathVariable Integer current,
                                                                  @PathVariable @Positive Integer size) {
        return emailService.publicEmailList(type,current,size);
    }

    @Operation(summary = "信件内容")
    @GetMapping("public/text/{emailId}")
    public Result<EmailPublicVo> publicEmailText(@PathVariable String emailId) {
        if (emailId.length() != 16) { return Result.build(null,500,"无此信件！"); }
        return emailService.publicEmailText(emailId);
    }

    @Operation(summary = "index 三个信件")
    @GetMapping("public/index")
    public Result publicEmailIndex() {
        return emailService.publicEmailIndex();
    }
}
