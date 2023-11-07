package top.woaibocai.model.entity.blog;



import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 
 * @TableName blog_tag
 */
@TableName(value ="blog_tag")
@Data
public class Tag implements Serializable {
    /**
     * 主键
     */
    @Schema(description = "主键")
    @TableId
    private Long id;

    /**
     * 标签名
     */
    @Schema(description = "标签名")
    private String tagName;

    /**
     * 备注
     */
    @Schema(description = "备注")
    private String remark;

    /**
     * 逻辑删除
     */
    @Schema(description = "逻辑删除")
    @TableLogic
    private Integer isDeleted;

    /**
     * 创建时间
     */
    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    @Schema(description = "修改时间")
    private LocalDateTime updateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}