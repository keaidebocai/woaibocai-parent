package top.woaibocai.model.vo.blog.other;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * @program: woaibocai-parent
 * @description: RSS返回属性
 * @author: woaibocai
 * @create: 2024-02-01 18:03
 **/
@Data
@Schema(description = "RSS返回属性")
public class RSSVo {
    @Schema(description = "title")
    private String title;
    @Schema(description = "url")
    private String url;
    @Schema(description = "时间")
    private String date;
    @Schema(description = "描述")
    private String description;
    @Schema(description = "标签")
    private List<String> tags;
}
