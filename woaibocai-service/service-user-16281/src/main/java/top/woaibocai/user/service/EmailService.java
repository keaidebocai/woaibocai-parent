package top.woaibocai.user.service;

import top.woaibocai.model.common.Result;
import top.woaibocai.model.dto.email.ContentDto;
import top.woaibocai.model.entity.email.Content;

/**
 * @program: woaibocai-parent
 * @description: 关于时光邮局用户的接口
 * @author: LikeBocai
 * @create 2024/7/18 13:26
 **/

public interface EmailService {
    Result emailWritingEmail(ContentDto content,String ip);
}
