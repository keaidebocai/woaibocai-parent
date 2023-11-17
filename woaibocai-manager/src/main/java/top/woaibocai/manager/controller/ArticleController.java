package top.woaibocai.manager.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import top.woaibocai.manager.service.ArticleService;
import top.woaibocai.model.common.Result;
import top.woaibocai.model.common.ResultCodeEnum;
import top.woaibocai.model.dto.QueryArticleCriteria;
import top.woaibocai.model.dto.UpdateArticleStatusDto;
import top.woaibocai.model.vo.manager.ArticlePageVo;

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
    public Result<IPage<ArticlePageVo>> findAllPage(@PathVariable Integer current,
                                                    @PathVariable Integer size,
                                                    @RequestHeader(name = "Authorization") String token,
                                                    @RequestBody QueryArticleCriteria queryArticleCriteria){
        String newToken = token.replace("Bearer ", "");
        return articleService.findPage(current,size,newToken,queryArticleCriteria);
    }
    @Operation(summary = "更新文章各个的状态")
    @PostMapping("updateArticleStatus")
    public Result updateArticleStatus(@RequestBody UpdateArticleStatusDto updateArticleStatusDto){
        articleService.updateArticleStatus(updateArticleStatusDto);
        return Result.build(null, ResultCodeEnum.SUCCESS);
    }
    @Operation(summary = "删除文章")
    @DeleteMapping("deleteById/{id}")
    public Result DeletedArticleById(@PathVariable Integer id){
        articleService.deletedArticleById(id);
        return Result.build(null, ResultCodeEnum.SUCCESS);
    }
}
