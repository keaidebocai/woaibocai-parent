package top.woaibocai.model.Do;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @program: woaibocai-parent
 * @description:
 * @author: woaibocai
 * @create: 2024-01-13 15:31
 **/
@Data
@Schema(description = "数据库临时接收")
public class KeyAndValue {
    @Schema(description = "key")
    private String myKey;
    @Schema(description = "value")
    private String myValue;
}
