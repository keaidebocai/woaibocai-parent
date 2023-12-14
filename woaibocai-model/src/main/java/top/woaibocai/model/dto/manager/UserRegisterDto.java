package top.woaibocai.model.dto.manager;

import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(description = "用户名")
    private String userName;
    @Schema(description = "密码")
    private String password;
    @Schema(description = "邮箱")
    private String email;
}
