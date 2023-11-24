package top.woaibocai.model.common;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 
 * @TableName user
 */
@TableName(value ="user")
@Data
public class User implements Serializable {
    /**
     * 主键
     */
    @Schema(description = "主键")
    @TableId
    private String id;

    /**
     * 用户名
     */
    @Schema(description = "用户名")
    private String userName;

    /**
     * 密码
     */
    @Schema(description = "密码")
    private String password;

    /**
     * 昵称
     */
    @Schema(description = "昵称")
    private String nickName;

    /**
     * 头像
     */
    @Schema(description = "头像")
    private String avatar;

    /**
     * 用户性别(0:男，1:女，2:沃尔玛购物袋，3:阿帕奇武装直升机)
     */
    @Schema(description = "用户性别(0:男，1:女，2:沃尔玛购物袋，3:阿帕奇武装直升机)")
    private String sex;

    /**
     * 邮箱
     */
    @Schema(description = "邮箱")
    private String email;

    /**
     * 手机号
     */
    @Schema(description = "手机号")
    private String phone;

    /**
     * 状态(0:正常,1:封禁)
     */
    @Schema(description = "状态(0:正常,1:封禁)")
    private String status;

    /**
     * refresh_token
     */
    @Schema(description = "refresh_token")
    private String RefreshToken;

    /**
     * 乐观锁
     */
    @Schema(description = "乐观锁")
    private String version;

    /**
     * 逻辑删除
     */
    @Schema(description = "逻辑删除")
    @TableLogic
    private Integer isDeleted;

    /**
     * 允许评论(0:允许，1：不允许)
     */
    @Schema(description = "允许评论(0:允许，1：不允许)")
    private Integer commontStatus;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @Schema(description = "修改时间")
    private LocalDateTime updateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

}