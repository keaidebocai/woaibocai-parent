package top.woaibocai.manager.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import top.woaibocai.manager.service.CommentService;
import top.woaibocai.model.common.Result;
import top.woaibocai.model.dto.manager.comment.QueryCommentDto;

@Tag(name = "评论管理接口")
@RestController
@RequestMapping("admin/api/manager/comment")
public class CommentController {
    @Resource
    private CommentService commentService;
    @Operation(summary = "评论列表")
    @PostMapping("getCommentList/{current}/{size}")
    public Result list(@PathVariable Integer current,
                       @PathVariable Integer size,
                       @RequestBody QueryCommentDto queryCommentDto){
        return commentService.list(current,size,queryCommentDto);
    }
}
