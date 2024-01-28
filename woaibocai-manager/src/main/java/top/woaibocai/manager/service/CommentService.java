package top.woaibocai.manager.service;

import top.woaibocai.model.common.Result;
import top.woaibocai.model.dto.manager.comment.QueryCommentDto;

public interface CommentService {
    Result list(Integer current, Integer size, QueryCommentDto queryCommentDto);

    Result deleteById(String id,String blogArticleId);
}
