package top.woaibocai.manager.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import top.woaibocai.manager.service.ArticleService;
import top.woaibocai.model.common.Result;
import top.woaibocai.model.Enum.ResultCodeEnum;
import top.woaibocai.model.dto.manager.article.QueryArticleCriteria;
import top.woaibocai.model.dto.manager.article.UpdateArticleStatusDto;
import top.woaibocai.model.dto.manager.article.WriteArticleDto;
import top.woaibocai.model.vo.manager.ArticleStatusPageVo;

/**
 * @program: woaibocai-parent
 * @description: 文章
 * @author: woaibocai
 * @create: 2023-11-08 14:01
 **/
@Tag(name = "文章管理接口")
@RestController
@RequestMapping("admin/api/manager/article")
public class ArticleController {
    @Resource
    private ArticleService articleService;

    @Operation(summary = "获取文章列表")
    @PostMapping("findAllPage/{current}/{size}")
    public Result<IPage<ArticleStatusPageVo>> findAllPage(@PathVariable Integer current,
                                                          @PathVariable Integer size,
                                                          @RequestBody QueryArticleCriteria queryArticleCriteria){
        return articleService.findPage(current,size,queryArticleCriteria);
    }
    @Operation(summary = "更新文章各个的状态")
    @PostMapping("updateArticleStatus")
    public Result updateArticleStatus(@RequestBody UpdateArticleStatusDto updateArticleStatusDto){
        articleService.updateArticleStatus(updateArticleStatusDto);
        return Result.build(null, ResultCodeEnum.SUCCESS);
    }
    @Operation(summary = "删除文章")
    @DeleteMapping("deleteById/{id}/{url}")
    public Result DeletedArticleById(@PathVariable String id,@PathVariable String url){
        articleService.deletedArticleById(id,url);
        return Result.build(null, ResultCodeEnum.SUCCESS);
    }
    @PostMapping("writeArticle")
    public Result test(@RequestBody WriteArticleDto writeArticleDto){
        return articleService.writeArticle(writeArticleDto);
    }
    @Operation(summary = "修改文章数据回显")
    @GetMapping("updateArticleData/{id}")
    public Result updateArticleData(@PathVariable String id){

        return articleService.updateArticleData(id);
    }
    @Operation(summary = "更新草稿")
    @PutMapping("updateArticle")
    public Result updateArticle(@RequestBody WriteArticleDto writeArticleDto){
        //给前端协商axios
        return articleService.updateArticle(writeArticleDto);
    }
}
