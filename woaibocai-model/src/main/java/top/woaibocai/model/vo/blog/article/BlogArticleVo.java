package top.woaibocai.model.vo.blog.article;

import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import top.woaibocai.model.vo.blog.tag.TagInfo;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @program: woaibocai-parent
 * @description: 首页文章分页
 * @author: woaibocai
 * @create: 2023-12-28 16:08
 **/
@Schema(description = "首页文章分页")
@Data
public class BlogArticleVo {
    /**
     * 主键
     */
    @Schema(description = "主键")
    @TableId
    private String id;

    /**
     * 发表的用户名
     */
    @Schema(description = "发表的用户名")
    private String userName;

    /**
     * 标题
     */
    @Schema(description = "文章表标题")
    private String title;

    /**
     * url
     */
    @Schema(description = "文章url")
    private String url;

    /**
     * 文章摘要
     */
    @Schema(description = "文章摘要")
    private String summary;

    /**
     * 文章内容
     */
    @Schema(description = "文章内容")
    private String content;

    /**
     * blog_category分类表id
     */
    @Schema(description = "文章的分类名")
    private String blogCategoryName;
    /**
     * blog_category分类表id
     */
    @Schema(description = "blog_category分类表id")
    private String categoryId;

    /**
     * 缩略图
     */
    @Schema(description = "缩略图")
    private String thumbnail;

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
     * 修改时间
     */
    @Schema(description = "修改时间")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime updateTime;

    @Schema(description = "文章字数")
    private Integer articleLength;

    @Schema(description = "阅读时长: articleLength/300")
    private Integer readingDuration;
    @Schema(description = "标签集合")
    private List<TagInfo> tags;
}
