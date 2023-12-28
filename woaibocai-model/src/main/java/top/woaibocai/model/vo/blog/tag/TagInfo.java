package top.woaibocai.model.vo.blog.tag;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @program: woaibocai-parent
 * @description: 标签信息
 * @author: woaibocai
 * @create: 2023-12-28 16:18
 **/
@Schema(description = "标签信息")
@Data
public class TagInfo {
    @Schema(description = "标签id")
    private String id;
    @Schema(description = "标签名")
    private String tagName;
    @Schema(description = "拥有此标签的文章数")
    private Integer thisTagHasArticleCount;
}
