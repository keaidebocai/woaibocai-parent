package top.woaibocai.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "友链添加dto")
@Data
public class InsertLinkDto {
    @Schema(description = "主键")
    private String id;
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
