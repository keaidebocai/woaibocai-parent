package top.woaibocai.model.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @program: woaibocai-parent
 * @description:
 * @author: woaibocai
 * @create: 2023-12-11 10:35
 **/
@Data
@Schema(description = "接收refresh_token")
public class AuthorizationsDto {
    @Schema(description = "refresh_token")
    private String refresh_token;
}
