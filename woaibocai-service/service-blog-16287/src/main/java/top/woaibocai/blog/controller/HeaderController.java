package top.woaibocai.blog.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.woaibocai.blog.service.HeaderService;
import top.woaibocai.model.common.Result;
import top.woaibocai.model.vo.blog.category.CategoryUrlNameIconVo;

import java.util.List;

/**
 * @program: woaibocai-parent
 * @description: 关于导航栏的一切
 * @author: woaibocai
 * @create: 2024-01-02 16:21
 **/
@Tag(name = "导航栏")
@RequestMapping("/api/blog/header")
@RestController
public class HeaderController {
    @Resource
    private HeaderService headerService;
    @Operation(summary = "导航数据")
    @GetMapping("getAllCategoryNameAndUrl")
    public Result<List<CategoryUrlNameIconVo>> getCategoryAndMenu(){
        return headerService.getAllCategoryNameAndUrl();
    }
}
