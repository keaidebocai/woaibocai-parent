package top.woaibocai.manager.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import top.woaibocai.manager.service.CategoryService;
import top.woaibocai.model.common.Result;
import top.woaibocai.model.dto.manager.category.QueryCategoryDto;
import top.woaibocai.model.dto.manager.category.UpdateCategoryDto;

@Tag(name = "分类管理")
@RestController
@RequestMapping("admin/api/manager/category")
public class CategoryController {
    @Resource
    private CategoryService categoryService;
    @Operation(summary = "分类列表")
    @PostMapping("list/{current}/{size}")
    public Result list(@PathVariable Integer current,
                       @PathVariable Integer size,
                       @RequestBody QueryCategoryDto queryCategoryDto){
        return categoryService.list(current,size,queryCategoryDto);
    }
    @Operation(summary = "删除")
    @DeleteMapping("delete/{id}")
    public Result deletedById(@PathVariable String id){
        return categoryService.deletedById(id);
    }
    @Operation(summary = "修改分类")
    @PutMapping("putOfCategory")
    public Result putOfCategory(@RequestBody UpdateCategoryDto updateCategoryDto){
        return categoryService.putOfCategory(updateCategoryDto);
    }
}
