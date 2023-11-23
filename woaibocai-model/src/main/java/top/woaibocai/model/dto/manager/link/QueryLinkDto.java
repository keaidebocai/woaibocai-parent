package top.woaibocai.model.dto.manager.link;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "友链查询dto")
@Data
public class QueryLinkDto {
    @Schema(description = "友链名")
    private String linkName;
}
