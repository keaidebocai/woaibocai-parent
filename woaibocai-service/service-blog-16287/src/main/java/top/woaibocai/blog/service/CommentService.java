package top.woaibocai.blog.service;

import top.woaibocai.model.common.Result;
import top.woaibocai.model.dto.blog.comment.OneCommentDto;
import top.woaibocai.model.dto.blog.comment.ReplyOneCommentDto;

public interface CommentService {
    Result getCommentByArticleId(String articleId,Long current,Long size);

    Result sendOneComment(OneCommentDto oneCommentDto);

    Result replyOneComment(ReplyOneCommentDto replyOneCommentVo);
}
