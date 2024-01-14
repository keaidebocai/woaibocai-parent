package top.woaibocai.model.vo.blog.comment;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @program: woaibocai-parent
 * @description: 一级评论返回实体类
 * @author: woaibocai
 * @create: 2024-01-13 14:17
 **/
@Data
@Schema(description = "一级评论返回实体类")
public class OneCommentVo {
    @Schema(description = "评论id")
    private String id;
    @Schema(description = "评论者的id")
    private String sendId;
    @Schema(description = "发送评论的头像")
    private String sendUserAvater;
    @Schema(description = "发送评论的用户名")
    private String sandUserNickName;
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @Schema(description = "time")
    private LocalDateTime createTime;

    @Schema(description = "内容")
    private String content;

    @Schema(description = "点赞量")
    private Long likeCount ;
    @Schema(description = "地点")
    private String address;
    @Schema(description = "子评论集合")
    private List<CommentDataVo> oneCommentVoList;
}
