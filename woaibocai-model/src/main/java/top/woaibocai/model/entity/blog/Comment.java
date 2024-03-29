package top.woaibocai.model.entity.blog;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 
 * @TableName blog_comment
 */
@TableName(value ="blog_comment")
@Data
public class Comment implements Serializable {
    /**
     * 主键
     */
    @Schema(description = "主键")
    @TableId
    private String id;

    /**
     * 0:文章,1有链
     */
    @Schema(description = "0:文章,1有链")
    private String type;

    /**
     * blog_article表id
     */
    @Schema(description = "blog_article表id")
    private String blogArticleId;

    /**
     * 父评论(0为根评论)
     */
    @Schema(description = "父评论(0为根评论)")
    private String parentId;

    /**
     * 评论内容
     */
    @Schema(description = "评论内容")
    private String content;

    /**
     * 这是谁发出的评论
     */
    @Schema(description = "这是谁发出的评论")
    private String toCommentUserId;

    /**
     * 点赞
     */
    @Schema(description = "点赞")
    private Integer commentLikeCount;

    /**
     * 给谁回复的
     */
    @Schema(description = "给谁回复的")
    private String toCommentId;

    /**
     * 逻辑删除
     */
    @Schema(description = "逻辑删除")
    @TableLogic
    private Integer isDeleted;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "创建时间")
    private LocalDateTime createTime;
    @Schema(description = "ip/归属地")
    private String address;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}