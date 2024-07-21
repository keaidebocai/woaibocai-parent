package top.woaibocai.model.vo.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @program: woaibocai-parent
 * @description: 博客前端用户信息
 * @author: woaibocai
 * @create: 2023-12-14 09:20
 **/
@Data
@Schema(description = "博客前端用户信息")
public class UserInfoVo {
    @Schema(description = "昵称")
    private String nickName;
    @Schema(description = "头像")
    private String avatar;
    @Schema(description = "性别 0:男，1:女，2:沃尔玛购物袋，3:阿帕奇武装直升机")
    private String sex;
    @Schema(description = "用户id")
    private String userId;
    @Schema(description = "用户邮箱")
    private String email;
}
