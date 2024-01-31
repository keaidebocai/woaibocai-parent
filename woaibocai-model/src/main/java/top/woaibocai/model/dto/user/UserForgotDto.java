package top.woaibocai.model.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @program: woaibocai-parent
 * @description: 忘记密码
 * @author: woaibocai
 * @create: 2024-01-31 21:21
 **/
@Data
@Schema(description = "注册数据接收")
public class UserForgotDto {
    @Schema(description = "用户名")
    private String userName;
    @Schema(description = "密码")
    private String password;
    @Schema(description = "邮箱")
    private String email;
    @Schema(description = "验证码")
    private String code;
}