package top.woaibocai.model.entity.blog;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
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
    @TableId
    private Long id;

    /**
     * 分类名
     */
    private String categoryName;

    /**
     * 分类父id(0为根)
     */
    private Long parentId;

    /**
     * 描述
     */
    private String description;

    /**
     * 逻辑删除
     */
    @TableLogic
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