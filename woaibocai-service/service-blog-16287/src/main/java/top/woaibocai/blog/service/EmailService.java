package top.woaibocai.blog.service;

import top.woaibocai.model.common.Result;
import top.woaibocai.model.vo.email.EmailPublicVo;
import top.woaibocai.model.vo.email.LikebocaiPageVo;

/**
 * @program: woaibocai-parent
 * @description: 时光邮局接口
 * @author: LikeBocai
 * @create 2024/7/22 15:13
 **/

public interface EmailService {
    Result<LikebocaiPageVo<EmailPublicVo>> publicEmailList(String type, Integer current, Integer size);

    Result<EmailPublicVo> publicEmailText(String emailId);

    Result publicEmailIndex();
}
