package top.woaibocai.model.dto.manager.article;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import top.woaibocai.model.vo.manager.TagVo;

import java.lang.reflect.Array;
import java.util.List;

@Schema(description = "接收写文章的数据")
@Data
public class WriteArticleDto {
    @Schema(description = "文章id")
    private String id;
    @Schema(description = "分类名称")
    private String blogCategoryId;
    @Schema(description = "标签")
    private List<TagVo> tags;
    @Schema(description = "文章标题")
    private String title;
    @Schema(description = "文章摘要")
    private String summary;
    @Schema(description = "缩略图")
    private String thumbnail;
    @Schema(description = "文章内容")
    private String content;
    @Schema(description = "发布")
    private String status;
    @Schema(description = "评论")
    private String isCommont;
    @Schema(description = "置顶")
    private String isTop;
    /**
     * url
     */
    @Schema(description = "文章的url")
    private String url;
}
