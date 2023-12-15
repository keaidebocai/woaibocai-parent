package top.woaibocai.model.vo.blog.category;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @program: woaibocai-parent
 * @description: 所有分类
 * @author: woaibocai
 * @create: 2023-12-15 11:00
 **/
@Schema(description = "所有分类")
@Data
public class CategoryAllVo {
    @Schema(description = "id")
    private String id;
    @Schema(description = "分类名")
    private String categoryName;
}