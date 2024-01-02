package top.woaibocai.model.entity.blog;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 
 * @TableName blog_article
 */
@TableName(value ="blog_article")
@Data
@Schema(description = "文章实体类")
public class Article implements Serializable {
    /**
     * 主键
     */
    @Schema(description = "主键")
    @TableId
    private String id;

    /**
     * user表的id
     */
    @Schema(description = "用户表id")
    private String userId;

    /**
     * 标题
     */
    @Schema(description = "文章表标题")
    private String title;
    /**
     * url
     */
    @Schema(description = "文章url")
    private String url;

    /**
     * 文章摘要
     */
    @Schema(description = "文章摘要")
    private String summary;

    /**
     * 文章内容
     */
    @Schema(description = "文章内容")
    private String content;

    /**
     * blog_category分类表id
     */
    @Schema(description = "log_category分类表id")
    private String blogCategoryId;

    /**
     * 缩略图
     */
    @Schema(description = "缩略图")
    private String thumbnail;

    /**
     * 是否置顶(0:否,1:是)
     */
    @Schema(description = "是否置顶(0:否,1:是)")
    private String isTop;

    /**
     * 浏览量
     */
    @Schema(description = "浏览量")
    private Long viewCount;

    /**
     * 是否评论(0:否,1:是)
     */
    @Schema(description = "是否评论(0:否,1:是)")
    private String isCommont;

    /**
     * 是否发布(0:否,1:是)
     */
    @Schema(description = "是否发布(0:否,1:是)")
    private String status;

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

    /**
     * 修改时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @Schema(description = "修改时间")
    private LocalDateTime updateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}