package top.woaibocai.model.vo.blog;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @program: woaibocai-parent
 * @description: 攒点概要的数据
 * @author: woaibocai
 * @create: 2023-12-27 21:44
 **/
@Schema(description = "站点概要的数据")
@Data
public class BlogInfoVo {
    @Schema(description = "文章数")
    private Long articleCount;
    @Schema(description = "标签数")
    private Long tagCount;
    @Schema(description = "分类数")
    private Long categoryCount;
    @Schema(description = "总文章浏览量")
    private Long articleViewCount;
}
