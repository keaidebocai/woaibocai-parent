package top.woaibocai.model.vo.email;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @program: woaibocai-parent
 * @description: 公开信列表返回类
 * @author: LikeBocai
 * @create: 2024/7/22 14:44
 **/
@Schema(description = "公开信-列表返回类")
@Data
public class EmailPublicVo {
    @Schema(description = "邮箱id/随机的邮箱id")
    private String id;

    @Schema(description = "是否公开 Y: 立即公开 W:寄达公开 N: 设为私密")
    private String isPublic;

    @Schema(description = "是否投递 Y 已投递 N 为投递")
    private String isDelivery;

    @Schema(description = "邮箱标题")
    private String title;

    @Schema(description = "邮件内容")
    private String content;

    @Schema(description = "写信时间")
    private String writingDate;

    @Schema(description = "投递时间")
    private String deliveryDate;

    @Schema(description = "经历的时间")
    private String useTime;

    @Schema(description = "点赞数")
    private String likeCount;

    @Schema(description = "头像url")
    private String url;

    @Schema(description = "用户id")
    private String userId;

    @Schema(description = "用户昵称")
    private String nickName;

    @Schema(description = "index图片地址")
    private String imgUrl;

}
