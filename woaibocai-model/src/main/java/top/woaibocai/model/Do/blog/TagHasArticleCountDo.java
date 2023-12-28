package top.woaibocai.model.Do.blog;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @program: woaibocai-parent
 * @description: 一个标签中有多少文章数
 * @author: woaibocai
 * @create: 2023-12-28 17:58
 **/
@Schema(description = "一个标签中有多少文章数")
@Data
public class TagHasArticleCountDo {
    @Schema(description = "标签id")
    private String tagId;
    @Schema(description = "文章数量")
    private Integer articleCount;
}
