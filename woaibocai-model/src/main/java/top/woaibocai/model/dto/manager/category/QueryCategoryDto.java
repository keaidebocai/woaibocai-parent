package top.woaibocai.model.dto.manager.category;

import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "分类列表查询条件")
@Data
public class QueryCategoryDto {
    @Schema(description = "主键")
    private String id;
    /**
     * 分类名
     */
    @Schema(description = "分类名")
    private String categoryName;
}
