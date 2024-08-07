package top.woaibocai.blog.controller;

import com.jthinking.common.util.ip.IPInfo;
import com.jthinking.common.util.ip.IPInfoUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import top.woaibocai.blog.service.CommentService;
import top.woaibocai.blog.utils.IPutil;
import top.woaibocai.common.feign.QCloudFeignClint;
import top.woaibocai.model.common.Result;
import top.woaibocai.model.Enum.ResultCodeEnum;
import top.woaibocai.model.dto.blog.comment.OneCommentDto;
import top.woaibocai.model.dto.blog.comment.ReplyOneCommentDto;

import java.io.IOException;
import java.util.List;

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
    @Resource
    private QCloudFeignClint qCloudFeignClint;
    @Operation(summary = "根据文章id获取评论")
    @GetMapping("/{articleId}/{current}/{size}")
    public Result getCommentByArticleId(@PathVariable String articleId,
                                        @PathVariable Long current,
                                        @PathVariable Long size) {
        return commentService.getCommentByArticleId(articleId,current,size);
    }
    @Operation(summary = "发送一级评论")
    @PostMapping("/auth/sendOneComment")
    public Result sendOneComment(@RequestBody @Valid OneCommentDto oneCommentDto,
                                                      @RequestHeader("114514") @NotNull(message = "用户id不能为空") String userId,
                                                      @RequestHeader("x-forwarded-for") String ip) {
        IPutil iPutil = new IPutil();
        IPInfo ipInfo = IPInfoUtils.getIpInfo(iPutil.getIpAddr(ip));
        String addree = ipInfo.getProvince();
        if (addree == null) {
            addree = ipInfo.getCountry();
        }
        oneCommentDto.setAddress(addree);
        oneCommentDto.setUserId(userId);
        return commentService.sendOneComment(oneCommentDto);
    }
    @Operation(summary = "回复一级评论")
    @PostMapping("/auth/replyOneComment")
    public Result replyOneComment(@RequestBody @Valid ReplyOneCommentDto replyOneCommentVo,
                                  @RequestHeader("114514") @NotNull(message = "用户id不能为空") String userId,
                                  @RequestHeader("x-forwarded-for") String ip) {
        IPutil iPutil = new IPutil();
        IPInfo ipInfo = IPInfoUtils.getIpInfo(iPutil.getIpAddr(ip));
        String addree = ipInfo.getProvince();
        if (addree == null) {
            addree = ipInfo.getCountry();
        }
        System.out.println(ip);
        replyOneCommentVo.setAddress(addree);
        replyOneCommentVo.setSendCommentUserId(userId);
        return commentService.replyOneComment(replyOneCommentVo);
    }

    @Operation(summary = "评论区上传图片")
    @PostMapping(value = "auth/uploadImageByComment")
    public Result<List<String>> uploadImageByComment(List<MultipartFile> files) throws IOException {
        List<String> urls = qCloudFeignClint.userUpload(files);
        return Result.build(urls,ResultCodeEnum.SUCCESS);
    }
    @Operation(summary = "点赞")
    @GetMapping("like/{commentId}")
    public Result<Long> likeThisComment(@PathVariable String commentId) {
        return commentService.likeThisComment(commentId);
    }
    @Operation(summary = "取消点赞")
    @GetMapping("quitLike/{commentId}")
    public Result<Long> quitLikeThisComment(@PathVariable String commentId) {
        return commentService.quitLikeThisComment(commentId);
    }
}
