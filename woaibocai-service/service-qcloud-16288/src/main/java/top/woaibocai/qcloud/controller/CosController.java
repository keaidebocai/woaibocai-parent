package top.woaibocai.qcloud.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import top.woaibocai.model.common.Result;
import top.woaibocai.model.common.ResultCodeEnum;
import top.woaibocai.qcloud.service.CosService;

import java.io.IOException;

@Tag(name = "cos文件上传")
@RestController
@RequestMapping("bcblog/cos/")
public class CosController {
    @Resource
    private CosService cosService;
    @Operation(summary = "文章图片上传")
    @PostMapping("uploadThumbnail")
    public Result uploadThumbnail(MultipartFile file) throws IOException {
        String url = cosService.uploadThumbnail(file);
        System.out.println(url);
        return Result.build(url, ResultCodeEnum.SUCCESS);
    }
}