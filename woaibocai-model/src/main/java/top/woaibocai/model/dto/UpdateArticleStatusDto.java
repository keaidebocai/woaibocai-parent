package top.woaibocai.model.dto;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @program: woaibocai-parent
 * @description: 更新文章各个状态
 * @author: woaibocai
 * @create: 2023-11-09 13:56
 **/
@Schema(description = "更新文章各个状态")
@Data
public class UpdateArticleStatusDto {
    /**
     * 主键
     */
    @Schema(description = "主键")
    @TableId
    private String id;
    /**
     * 标题
     */
    @Schema(description = "文章表标题")
    private String title;
    /**
     * 是否置顶(0:否,1:是)
     */
    @Schema(description = "是否置顶(0:否,1:是)")
    private String isTop;
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


}
