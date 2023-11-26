package top.woaibocai.model.others.manager;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "分类选择器所需的子元素")
public class CategoryBySelector {
    @Schema(description = "分类真实的id")
    private String value;
    @Schema(description = "分类真是的分类名")
    private String label;

}
