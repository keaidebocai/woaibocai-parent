package top.woaibocai.blog.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.woaibocai.blog.service.BlogInfoService;
import top.woaibocai.common.feign.QCloudFeignClint;
import top.woaibocai.model.common.Result;
import top.woaibocai.model.vo.blog.BlogInfoVo;
import top.woaibocai.model.vo.blog.tag.TagCloudVo;

import java.util.List;

/**
 * @program: woaibocai-parent
 * @description: 博客基本信息
 * @author: woaibocai
 * @create: 2023-12-26 17:51
 **/
@Tag(name = "博客基本信息")
@RestController
@RequestMapping("/api/blog/info")
public class BlogInfoController {
    @Resource
    private BlogInfoService blogInfoService;

    @Resource
    private QCloudFeignClint qCloudFeignClint;
    @Operation(summary = "站点概要文章数之类的")
    @GetMapping("blogInfo")
    public Result<BlogInfoVo> blogInfo() {
        return blogInfoService.blogInfo();
    }
    @Operation(summary = "标签云数据")
    @GetMapping("tagCloud")
    public Result<List<TagCloudVo>> tagCloud(){
        return blogInfoService.tagCloud();
    }
    @Operation(summary = "远程调用:test")
    @GetMapping("test")
    public String test(){
        String testString = qCloudFeignClint.getTestString();
        return testString;
    }

}
