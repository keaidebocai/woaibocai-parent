package top.woaibocai.model.entity.blog;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @program: woaibocai-parent
 * @description: 菜单实体类
 * @author: woaibocai
 * @create: 2023-12-15 14:21
 **/
@TableName(value ="blog_menu")
@Data
public class Menu implements Serializable {
    /**
     * 主键
     */
    @Schema(description = "主键")
    @TableId
    private String id;

    /**
     * 标签名
     */
    @Schema(description = "菜单名")
    private String menuName;
    @Schema(description = "路径")
    private String url;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
