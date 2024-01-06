package top.woaibocai.blog.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.woaibocai.blog.service.ArticleService;
import top.woaibocai.model.common.Result;
import top.woaibocai.model.vo.blog.article.BlogArticleVo;
import java.util.Map;

/**
 * @program: woaibocai-parent
 * @description: 文章相关
 * @author: woaibocai
 * @create: 2023-12-28 15:59
 **/
@Tag(name = "文章相关接口")
@RestController
@RequestMapping("/api/blog/article")
public class ArticleController {
    @Resource
    private ArticleService articleService;
    @Operation(summary = "首页文章分页")
    @GetMapping("indexArticlePage/{current}/{size}")
    public Result<Map<String,Object>> indexArticlePage(@PathVariable Integer current,
                                                       @PathVariable Integer size) {
        return articleService.indexArticlePage(current,size);
    }
    @Operation(summary = "根据文章id获取文章")
    @GetMapping("getArticleByUrl/{url}")
    public Result<BlogArticleVo> getArticleByUrl(@PathVariable String url) {
        return articleService.getArticleByUrl(url);
    }
    @Operation(summary = "根据tagUrl分页")
    @GetMapping("articlePageBytagUrl/{tagUrl}/{current}/{size}")
    public Result<Map<String,Object>> articlePageBytagUrl(@PathVariable String tagUrl,
                                                          @PathVariable Integer current,
                                                          @PathVariable Integer size) {
        return articleService.articlePageBytagUrl(tagUrl,current,size);
    }
}
