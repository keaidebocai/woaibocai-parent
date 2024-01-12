package top.woaibocai.model.dto.blog.comment;

import io.swagger.v3.oas.annotations.media.Schema;
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

    @Schema(description = "文章id")
    private String articleId;
    @Schema(description = "内容")
    private String content;

}
