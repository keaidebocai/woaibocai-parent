package top.woaibocai.blog.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.woaibocai.blog.service.LinkService;
import top.woaibocai.model.common.Result;
import top.woaibocai.model.vo.blog.link.LinkVo;

import java.util.List;

/**
 * @program: woaibocai-parent
 * @description: 友链
 * @author: woaibocai
 * @create: 2024-01-24 12:22
 **/
@Tag(name = "友链")
@RequestMapping("/api/blog/link")
@RestController
public class LinkController {
    @Resource
    private LinkService linkService;
    @Operation(summary = "所有友链")
    @GetMapping("getAllLink")
    public Result<List<LinkVo>> getAllLink() {
        return linkService.getAllLink();
    }
}
