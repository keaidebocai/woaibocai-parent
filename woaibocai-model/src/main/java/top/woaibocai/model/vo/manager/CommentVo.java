package top.woaibocai.model.vo.manager;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Schema(description = "评论管理返回类")
public class CommentVo {
    @Schema(description = "主键")
    private String id;
    @Schema(description = "评论内容")
    private String content;
    @Schema(description = "文章id")
    private String blogArticleId;
    @Schema(description = "文章名")
    private String articleTitle;
    @Schema(description = "发出评论的id")
    private String toCommentUserId;
    @Schema(description = "发出评论的昵称")
    private String toCommentUserNickName;
    @Schema(description = "接受人的id")
    private String toCommentId;
    @Schema(description = "接受人的昵称")
    private String toCommentNickName;
    @Schema(description = "点赞数")
    private Integer commentLikeCount;
    @Schema(description = "创建时间")
    private LocalDateTime createTime;
}
