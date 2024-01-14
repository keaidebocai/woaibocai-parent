package top.woaibocai.blog.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;
import top.woaibocai.blog.service.CommentService;
import top.woaibocai.blog.utils.IPutil;
import top.woaibocai.model.common.Result;
import top.woaibocai.model.common.ResultCodeEnum;
import top.woaibocai.model.dto.blog.comment.OneCommentDto;
import top.woaibocai.model.dto.blog.comment.ReplyOneCommentDto;

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
    @GetMapping("/{articleId}/{current}/{size}")
    public Result getCommentByArticleId(@PathVariable String articleId,
                                        @PathVariable Long current,
                                        @PathVariable Long size) {
        return commentService.getCommentByArticleId(articleId,current,size);
    }
    @Operation(summary = "发送一级评论")
    @PostMapping("/auth/sendOneComment")
    public Result sendOneComment(@RequestBody OneCommentDto oneCommentDto,
                                                      @RequestHeader("114514") String userId,
                                                      HttpServletRequest request) {
        if (oneCommentDto.getArticleId().isEmpty() | oneCommentDto.getContent().isEmpty()) {
            return Result.build("虚空评论，出现异常！", ResultCodeEnum.DATA_ERROR);
        }
        IPutil iPutil = new IPutil();
        oneCommentDto.setAddress(iPutil.getIpAddr(request));
        oneCommentDto.setUserId(userId);
        return commentService.sendOneComment(oneCommentDto);
    }
    @Operation(summary = "回复一级评论")
    @PostMapping("/auth/replyOneComment")
    public Result replyOneComment(@RequestBody ReplyOneCommentDto replyOneCommentVo,
                                  @RequestHeader("114514") String userId,
                                  HttpServletRequest request) {
        if (replyOneCommentVo.getReplyCommentId().isEmpty() |
            replyOneCommentVo.getReplyCommentUserId().isEmpty() |
            replyOneCommentVo.getContent().isEmpty() |
            replyOneCommentVo.getArticleId().isEmpty() |
            replyOneCommentVo.getParentId().isEmpty()) {
            return Result.build("虚空评论，数据异常！", ResultCodeEnum.DATA_ERROR);
        }
        IPutil iPutil = new IPutil();
        replyOneCommentVo.setSendCommentUserId(userId);
        replyOneCommentVo.setAddress(iPutil.getIpAddr(request));
        return commentService.replyOneComment(replyOneCommentVo);
    }
}
