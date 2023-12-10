package top.woaibocai.model.dto.manager;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @program: woaibocai-parent
 * @description: 注册dto
 * @author: woaibocai
 * @create: 2023-12-10 08:41
 **/
@Data
@Schema(description = "注册数据接收")
public class UserRegisterDto {
    @NotNull
    @Schema(description = "用户名")
    private String userName;
    @Max(value = 18,message = "最大长度不能超过18位")
    @Schema(description = "密码")
    private String password;
    @Email(message = "邮箱格式不正确")
    @Schema(description = "邮箱")
    private String email;
}
