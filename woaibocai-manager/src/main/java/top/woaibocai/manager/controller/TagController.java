package top.woaibocai.manager.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import top.woaibocai.manager.service.TagService;
import top.woaibocai.model.common.Result;
import top.woaibocai.model.dto.manager.tag.QueryTagDto;


@Tag(name = "标签管理接口")
@RestController
@RequestMapping("admin/api/manager/tag")
public class TagController {
    @Resource
    private TagService tagService;
    @Operation(summary = "条件分页查询")
    @PostMapping("findByTageName/{current}/{size}")
    public Result findByTageName(@PathVariable Integer current,
                                 @PathVariable Integer size,
                                 @RequestBody QueryTagDto queryTagDto){
        return tagService.findByTageName(current,size,queryTagDto);
    }
    @Operation(summary = "添加/更改标签")
    @PostMapping("insertTag")
    public Result insertTag(@RequestBody QueryTagDto queryTagDto){
        return tagService.insertTag(queryTagDto);
    }
    @Operation(summary = "删除标签")
    @DeleteMapping("deleteById/{id}")
    public Result deleted(@PathVariable String id){
        return tagService.deleted(id);
    }
    @Operation(summary = "返回所有标签名和标签id")
    @GetMapping("getAllTagAndId")
    public Result getAllTagAndId(){
        return tagService.getAllTagAndId();
    }
}
