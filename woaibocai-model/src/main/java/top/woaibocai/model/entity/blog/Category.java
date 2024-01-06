package top.woaibocai.model.entity.blog;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 
 * @TableName blog_category
 */
@TableName(value ="blog_category")
@Data
public class Category implements Serializable {
    /**
     * 主键
     */
    @Schema(description = "主键")
    @TableId
    private String id;

    /**
     * 分类名
     */
    @Schema(description = "分类名")
    private String categoryName;

    /**
     * 分类父id(0为根)
     */
    @Schema(description = "分类url")
    private String categoryUrl;

    /**
     * 描述
     */
    @Schema(description = "描述")
    private String description;

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