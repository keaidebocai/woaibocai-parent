package top.woaibocai.blog.service;

import top.woaibocai.model.common.Result;

public interface CommentService {
    Result getCommentByArticleId(String articleId);
}
