package top.woaibocai.model.vo.blog.comment;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * @program: woaibocai-parent
 * @description: 评论区数据
 * @author: woaibocai
 * @create: 2024-01-10 17:00
 **/
@Data
@Schema(description = "每条评论")
public class CommentDataVo {
    @Schema(description = "评论id")
    private String id;
    @Schema(description = "评论者的id")
    private String sendId;
    @Schema(description = "发送评论的头像")
    private String sendUserAvater;
    @Schema(description = "发送评论的用户名")
    private String sandUserName;

    @Schema(description = "被回复者的id")
    private String replyId;
    @Schema(description = "被回复者的头像")
    private String replyUserAvater;

    @Schema(description = "被回复者的用户名")
    private String replyUserName;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @Schema(description = "time")
    private String createTime;

    @Schema(description = "内容")
    private String content;

    @Schema(description = "父评论id")
    private String parentId;

    @Schema(description = "点赞量")
    private Long likeCount ;

//    @Schema(description = "子评论集合")
//    private List<CommentVo> subList;

    @Schema(description = "子评论id集合")
    private List<String> sunIdList;

    @Schema(description = "地点")
    private String address;

}
