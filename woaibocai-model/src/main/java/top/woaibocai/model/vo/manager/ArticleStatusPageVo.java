package top.woaibocai.model.vo.manager;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @program: woaibocai-parent
 * @description: 后端管理的问政管理返回值
 * @author: woaibocai
 * @create: 2023-11-08 14:11
 **/
@Data
@Schema(description = "文章状态管理返回类")
public class ArticleStatusPageVo {
    @Schema(description = "主键")
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
     * 浏览量
     */
    @Schema(description = "浏览量")
    private Long viewCount;
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
     * 逻辑删除
     */
    @Schema(description = "逻辑删除")
    private Integer isDeleted;
    /**
     * 创建时间
     */
    @Schema(description = "创建时间")
    private LocalDateTime createTime;
    /**
     * 修改时间
     */
    @Schema(description = "修改时间")
    private LocalDateTime updateTime;
    /**
     * url
     */
    @Schema(description = "文章的url")
    private String url;
}
