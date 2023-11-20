package top.woaibocai.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class QueryTagDto {
    @Schema(description = "主键")
    private String id;
    @Schema(description = "查询的字段")
    private String tagName;
    @Schema(description = "标签备注")
    private String remark;
}
