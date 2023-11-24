package top.woaibocai.model.dto.manager.comment;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "查询评论")
@Data
public class QueryCommentDto {
    @Schema(description = "评论关键词")
    private String content;
    @Schema(description = "文章标题关键词")
    private String articleTitle;
    @Schema(description = "发评论关键词")
    private String toCommentUserNickName;
    @Schema(description = "收评论的关键处")
    private String toCommentNickName;
}
