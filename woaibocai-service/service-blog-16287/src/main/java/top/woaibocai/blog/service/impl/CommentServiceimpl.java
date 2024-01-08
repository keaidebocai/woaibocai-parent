package top.woaibocai.blog.service.impl;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import top.woaibocai.blog.mapper.CommentMapper;
import top.woaibocai.blog.service.CommentService;
import top.woaibocai.model.common.Result;

/**
 * @program: woaibocai-parent
 * @description: 评论接口的实现类
 * @author: woaibocai
 * @create: 2024-01-08 13:14
 **/
@Service
public class CommentServiceimpl implements CommentService {
    @Resource
    private CommentMapper commentMapper;
    @Override
    public Result getCommentByArticleId(String articleId) {

        return null;
    }
}
