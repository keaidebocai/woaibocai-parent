package top.woaibocai.model.Do.blog;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * @program: woaibocai-parent
 * @description: 文章对应的所有标签
 * @author: woaibocai
 * @create: 2023-12-28 18:58
 **/
@Schema(description = "文章对应的所有标签")
@Data
public class ArticleHasTagsDo {
    @Schema(description = "文章id")
    private String articleId;
    @Schema(description = "tagIdListString")
    private String tagIdListString;
    @Schema(description = "所有标签的集合")
    private List<String> tagIdList;
}
