package top.woaibocai.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "列表查询条件")
@Data
public class QueryArticleCriteria {
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
    /**
     * 排序(1:时间,0:浏览量)
     */
    @Schema(description = "排序(1:时间,0:浏览量)")
    private String orderBy;

}
