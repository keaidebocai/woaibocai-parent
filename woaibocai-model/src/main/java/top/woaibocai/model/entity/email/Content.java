package top.woaibocai.model.entity.email;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 
 * @TableName email_content
 */
@TableName(value ="email_content")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Content implements Serializable {
    /**
     * 16位字符串的主键
     */
    @TableId(value = "id")
    private String id;

    /**
     * 可以是空
     */
    @TableField(value = "user_id")
    private String userId;

    /**
     * 邮件标题
     */
    @TableField(value = "title")
    private String title;

    /**
     * 发送者邮箱
     */
    @TableField(value = "sender_email")
    private String senderEmail;

    /**
     * 收信者邮箱
     */
    @TableField(value = "recipient_email")
    private String recipientEmail;

    /**
     * Y 已投递 N 未投递
     */
    @TableField(value = "is_delivery")
    private String isDelivery;

    /**
     * Y 即刻公开 W 寄达公开 N 不公开
     */
    @TableField(value = "is_public")
    private String isPublic;

    /**
     * 信件内容
     */
    @TableField(value = "content")
    private String content;
    /**
     * 点赞数
     */
    @TableField(value = "like_count")
    private Long likeCount;

    /**
     * 写信时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(value = "writing_email_time")
    private LocalDateTime writingEmailTime;

    /**
     * 投递时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(value = "delivery_time")
    private LocalDateTime deliveryTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}