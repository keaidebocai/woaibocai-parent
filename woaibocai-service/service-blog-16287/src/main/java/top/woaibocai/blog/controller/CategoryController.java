package top.woaibocai.blog.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.woaibocai.blog.service.CategoryService;
import top.woaibocai.model.common.Result;
import top.woaibocai.model.vo.blog.category.CategoryAllVo;

import java.util.List;

/**
 * @program: woaibocai-parent
 * @description: 分类
 * @author: woaibocai
 * @create: 2023-12-15 10:45
 **/
@Tag(name = "分类")
@RestController
@RequestMapping("/api/blog/category")
public class CategoryController {
    @Resource
    private CategoryService categoryService;
    @Operation(summary = "所有分类接口")
    @GetMapping
    public Result<List<CategoryAllVo>> getAllCategory() {
        return categoryService.getAllCategory();
    }
}
