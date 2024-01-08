package top.woaibocai.blog.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.woaibocai.blog.service.CommentService;
import top.woaibocai.model.common.Result;

/**
 * @program: woaibocai-parent
 * @description: 评论业务
 * @author: woaibocai
 * @create: 2024-01-08 13:12
 **/
@Tag(name = "评论接口")
@RestController
@RequestMapping("/api/blog/comment")
public class CommentController {
    @Resource
    private CommentService commentService;
    @Operation(summary = "根据文章id获取评论")
    @GetMapping("/{articleId}")
    public Result getCommentByArticleId(@PathVariable String articleId) {
        return commentService.getCommentByArticleId(articleId);
    }
}
