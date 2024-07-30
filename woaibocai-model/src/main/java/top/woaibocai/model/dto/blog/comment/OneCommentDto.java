package top.woaibocai.model.dto.blog.comment;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @program: woaibocai-parent
 * @description: 接收发送评论数据
 * @author: woaibocai
 * @create: 2024-01-12 17:42
 **/
@Data
@Schema(description = "接收发送评论数据")
public class OneCommentDto {

    @NotNull(message = "文章id不能为空")
    @Schema(description = "文章id")
    private String articleId;

    @NotNull(message = "评论内容不能为空")
    @Schema(description = "内容")
    private String content;

    @Schema(description = "评论者id")
    private String userId;

    @Schema(description = "ip")
    private String address;

}
