package top.woaibocai.model.entity.blog;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 
 * @TableName blog_article
 */
@TableName(value ="blog_article")
@Data
public class Article implements Serializable {
    /**
     * 主键
     */
    @TableId
    private Long id;

    /**
     * user表的id
     */
    private Long userId;

    /**
     * 标题
     */
    private String title;

    /**
     * 文章摘要
     */
    private String summary;

    /**
     * 文章内容
     */
    private String content;

    /**
     * blog_category分类表id
     */
    private Long blogCategoryId;

    /**
     * 缩略图
     */
    private String thumbnail;

    /**
     * 是否置顶(0:否,1:是)
     */
    private String isTop;

    /**
     * 浏览量
     */
    private Long viewCount;

    /**
     * 是否评论(0:否,1:是)
     */
    private String isCommont;

    /**
     * 是否发布(0:否,1:是)
     */
    private String status;

    /**
     * 逻辑删除
     */
    private Integer isDeleted;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    private LocalDateTime updateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}