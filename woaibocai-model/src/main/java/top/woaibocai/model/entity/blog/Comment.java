package top.woaibocai.model.entity.blog;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
    @TableId
    private Long id;

    /**
     * 0:文章,1有链
     */
    private String type;

    /**
     * blog_article表id
     */
    private Long blogArticleId;

    /**
     * 父评论(0为根评论)
     */
    private Long parentId;

    /**
     * 评论内容
     */
    private String content;

    /**
     * 这是谁发出的评论
     */
    private Long toCommentUserId;

    /**
     * 点赞
     */
    private Integer commentLikeCount;

    /**
     * 给谁回复的
     */
    private Long toCommentId;

    /**
     * 逻辑删除
     */
    private Integer isDeleted;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}