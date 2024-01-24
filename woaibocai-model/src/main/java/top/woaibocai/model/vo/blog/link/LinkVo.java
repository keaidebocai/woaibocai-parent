package top.woaibocai.model.vo.blog.link;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @program: woaibocai-parent
 * @description: 友链的vo类
 * @author: woaibocai
 * @create: 2024-01-24 12:20
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "友链的返回实体类")
public class LinkVo {
    /**
     * 友链名称
     */
    @Schema(description = "友链名称")
    private String linkName;

    /**
     * logo链接
     */
    @Schema(description = "logo链接")
    private String logo;

    /**
     * 描述
     */
    @Schema(description = "描述")
    private String description;

    /**
     * 网络地址
     */
    @Schema(description = "网络地址")
    private String address;

}
