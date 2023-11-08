package top.woaibocai.manager.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.woaibocai.manager.service.ArticleService;
import top.woaibocai.model.common.Result;
import top.woaibocai.model.entity.blog.Article;
import top.woaibocai.model.vo.manager.ArticlePageVo;

import java.util.List;

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
    @GetMapping("findAllPage/{current}/{size}")
    public Result<IPage<ArticlePageVo>> findAllPage(@PathVariable Integer current,
                                                    @PathVariable Integer size){
        return articleService.findPage(current,size);
    }
}
