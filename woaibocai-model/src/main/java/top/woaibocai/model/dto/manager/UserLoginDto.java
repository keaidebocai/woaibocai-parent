package top.woaibocai.model.dto.manager;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @program: woaibocai-parent
 * @description: 接受前端登录信息
 * @author: woaibocai
 * @create: 2023-11-07 13:53
 **/
@Schema(description = "登录信息")
@Data
public class UserLoginDto {
    @Schema(description = "用户名")
    private String userName;

    /**
     * 密码
     */
    @Schema(description = "密码")
    private String password;
}
