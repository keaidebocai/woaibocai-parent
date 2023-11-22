package top.woaibocai.model.dto;

import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "友链审核dto")
@Data
public class LinkPutStatusDto {
    /**
     * 主键
     */
    @Schema(description = "主键")
    @TableId
    private String id;
    /**
     * 状态
     */
    @Schema(description = "状态(1:通过,0:不通过)")
    private String status;


}
