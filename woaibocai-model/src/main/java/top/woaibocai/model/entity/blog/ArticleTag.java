package top.woaibocai.model.entity.blog;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 
 * @TableName blog_article_tag
 */
@TableName(value ="blog_article_tag")
@Data
public class ArticleTag implements Serializable {
    /**
     * 主键
     */
    @Schema(description = "主键")
    @TableId
    private Long id;

    /**
     * 文章id
     */
    @Schema(description = "文章id")
    private Long articleId;

    /**
     * 标签id
     */
    @Schema(description = "标签id")
    private Long tagId;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

}