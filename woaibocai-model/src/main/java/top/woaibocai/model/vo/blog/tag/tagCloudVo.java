package top.woaibocai.model.vo.blog.tag;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @program: woaibocai-parent
 * @description: 标签云
 * @author: woaibocai
 * @create: 2024-01-07 21:18
 **/
@Data
@Schema(description = "标签云")
public class tagCloudVo {
    @Schema(description = "标签名")
    private String tagName;

    /**
     * 标签url
     */
    @Schema(description = "标签url")
    private String tagUrl;

    /**
     * 标签颜色#000000
     */
    @Schema(description = "标签颜色#000000")
    private String color;
}
