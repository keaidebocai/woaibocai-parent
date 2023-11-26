package top.woaibocai.model.vo.manager;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "前端提示存在的tag")
@Data
public class TagVo {
    @Schema(description = "articleTagId")
    private String articleTagId;
    @Schema(description = "标签id")
    private String id;
    @Schema(description = "标签name")
    private String name;
}
