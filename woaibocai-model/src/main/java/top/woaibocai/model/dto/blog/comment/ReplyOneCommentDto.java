package top.woaibocai.model.dto.blog.comment;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @program: woaibocai-parent
 * @description: 回复一级评论
 * @author: woaibocai
 * @create: 2024-01-13 17:28
 **/
@Data
@Schema(description = "回复一级评论")
public class ReplyOneCommentDto {

    @NotNull(message = "回复评论的id不能为空")
    @Schema(description = "回复评论的id")
    private String replyCommentId;

    @Schema(description = "发送者id")
    private String sendCommentUserId;

    @NotNull(message = "回复评论的id不能为空")
    @Schema(description = "被回复的用户id")
    private String replyCommentUserId;

    @NotNull(message = "内容不能为空")
    @Schema(description = "内容")
    private String content;

    @Schema(description = "ip")
    private String address;

    @NotNull(message = "文章id不能为空")
    @Schema(description = "文章id")
    private String articleId;

    @NotNull(message = "父评论不能为空")
    @Schema(description = "父评论")
    private String parentId;
}
