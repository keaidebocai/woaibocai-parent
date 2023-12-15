package top.woaibocai.blog.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.woaibocai.blog.service.MenuService;
import top.woaibocai.model.common.Result;

/**
 * @program: woaibocai-parent
 * @description: 菜单
 * @author: woaibocai
 * @create: 2023-12-15 12:56
 **/
@Tag(name = "菜单")
@RestController
@RequestMapping("/api/blog/menu")
public class MenuController {
    @Resource
    private MenuService menuService;
    @Operation(summary = "获取所有发布的菜单")
    @GetMapping
    public Result getAllMenu(){
        return menuService.getAllMenu();
    }
}
