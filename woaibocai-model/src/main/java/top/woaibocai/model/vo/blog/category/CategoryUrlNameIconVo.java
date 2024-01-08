package top.woaibocai.model.vo.blog.category;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @program: woaibocai-parent
 * @description: 所有分类
 * @author: woaibocai
 * @create: 2023-12-15 11:00
 **/
@Schema(description = "所有分类")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryUrlNameIconVo {
    @Schema(description = "icon-url")
    private String categoryIconUrl;
    @Schema(description = "分类名")
    private String categoryName;
    @Schema(description = "url")
    private String categoryUrl;
    @Schema(description = "文章数量")
    private Integer count;
}
