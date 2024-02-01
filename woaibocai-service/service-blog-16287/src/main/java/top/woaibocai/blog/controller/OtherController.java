package top.woaibocai.blog.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.woaibocai.blog.service.OtherService;
import top.woaibocai.model.common.Result;
import top.woaibocai.model.vo.blog.other.RSSVo;
import top.woaibocai.model.vo.blog.other.Sitemap;

import java.util.List;

/**
 * @program: woaibocai-parent
 * @description: 杂七杂八
 * @author: woaibocai
 * @create: 2024-01-24 19:09
 **/
@Tag(name = "杂七杂八")
@RequestMapping("/api/blog/other")
@RestController
public class OtherController {
    @Resource
    private OtherService otherService;
    @Operation(summary = "关于")
    @GetMapping("about")
    public Result<String> about() {
        return otherService.about();
    }
    @Operation(summary = "友情链接")
    @GetMapping("link")
    public Result<String> link() {
        return otherService.link();
    }
    @Operation(summary = "sitemap")
    @GetMapping("sitemap")
    public List<Sitemap> sitemaps() {
        return otherService.sitemap();
    }
    @Operation(summary = "RSS")
    @GetMapping("RSS")
    public List<RSSVo> RSS(){
        return otherService.RSS();
    }
}
