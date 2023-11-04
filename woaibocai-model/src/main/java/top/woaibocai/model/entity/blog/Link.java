package top.woaibocai.model.entity.blog;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 
 * @TableName blog_link
 */
@TableName(value ="blog_link")
@Data
public class Link implements Serializable {
    /**
     * 主键
     */
    @TableId
    private Long id;

    /**
     * 友链名称
     */
    private String linkName;

    /**
     * logo链接
     */
    private String logo;

    /**
     * 描述
     */
    private String description;

    /**
     * 网络地址
     */
    private String address;

    /**
     * 审核状态(0:不通过,1:通过)
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