package top.woaibocai.model.dto.manager.category;

import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
@Schema(description = "修改分类")
@Data
public class UpdateCategoryDto {
    /**
     * 主键
     */
    @Schema(description = "主键")
    private String id;

    /**
     * 分类名
     */
    @Schema(description = "分类名")
    private String categoryName;

    /**
     * 描述
     */
    @Schema(description = "描述")
    private String description;

    /**
     * icon url
     */
    @Schema(description = "icon url")
    private String categoryIconUrl;
    /**
     * url
     */
    @Schema(description = "url")
    private String categoryUrl;

}
