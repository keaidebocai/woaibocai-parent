package top.woaibocai.model.dto.email;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * @program: woaibocai-parent
 * @description: ContentDto
 * @author: LikeBocai
 * @create: 2024/7/19 18:11
 **/
@Data
public class ContentDto {
    /**
     * 16位字符串的主键
     */
    private String id;

    /**
     * 可以是空
     */
    private String userId;

    /**
     * 邮件标题
     */
    @NotNull(message = "邮件标题不能为空")
    private String title;

    /**
     * 发送者邮箱
     */
    @NotNull(message = "寄信人不能为空")
    @Email
    private String senderEmail;

    /**
     * 收信者邮箱
     */
    @NotNull(message = "收信人不能为空")
    @Email
    private String recipientEmail;

    /**
     * Y 已投递 N 未投递
     */
    private String isDelivery;

    /**
     * Y 即刻公开 W 寄达公开 N 不公开
     */
    @Pattern(regexp = "^[YWN]$", message = "是否公开仅允许选择Y、W、N")
    private String isPublic;

    /**
     * 信件内容
     */
    @NotNull(message = "邮件内容不能为空")
    private String content;
    /**
     * 点赞数
     */
    private Long likeCount;

    /**
     * 写信时间
     */
    private Date writingEmailTime;

    /**
     * 投递时间
     */
    @Future
    private Date deliveryTime;
}
