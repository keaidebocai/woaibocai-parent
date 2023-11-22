package top.woaibocai.manager.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import top.woaibocai.manager.service.LinkService;
import top.woaibocai.model.common.Result;
import top.woaibocai.model.dto.InsertLinkDto;
import top.woaibocai.model.dto.LinkPutStatusDto;
import top.woaibocai.model.dto.QueryLinkDto;

@Tag(name = "友链管理")
@RestController
@RequestMapping("admin/api/manager/link/")
public class LinkController {

    @Resource
    private LinkService linkService;

    @Operation(summary = "友链列表")
    @PostMapping("list/{current}/{size}")
    public Result findAllList(@PathVariable Integer current,
                              @PathVariable Integer size,
                              @RequestBody QueryLinkDto queryLinkDto){
        return linkService.findAllList(current,size,queryLinkDto);
    }
    @Operation(summary = "更新审核状态")
    @PutMapping("putStatus")
    public Result putStatus(@RequestBody LinkPutStatusDto linkPutStatusDto){
        return linkService.putStatus(linkPutStatusDto);
    }
    @Operation(summary = "删除友链")
    @DeleteMapping("delete/{id}")
    public Result deleteById(@PathVariable String id){
        return linkService.deleteById(id);
    }
    @Operation(summary = "添加友链")
    @PostMapping("insertLink")
    public Result insertLink(@RequestBody InsertLinkDto insertLinkDto){
        return linkService.insertLink(insertLinkDto);
    }
}
