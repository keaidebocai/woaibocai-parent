package top.woaibocai.model.DO.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @program: woaibocai-parent
 * @description: 登录相关user信息
 * @author: woaibocai
 * @create: 2023-12-09 21:35
 **/
@Data
@Schema(description = "登录相关user信息")
public class UserLoginDo {
    @Schema(description = "id")
    private String id;
    @Schema(description = "密码")
    private String password;
    @Schema(description = "refresh_token")
    private String refreshToken;
    @Schema(description = "状态 0: 正常, 1: 封禁")
    private String status;
}
