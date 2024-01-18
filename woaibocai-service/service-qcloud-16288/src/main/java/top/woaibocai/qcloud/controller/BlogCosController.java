package top.woaibocai.qcloud.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import top.woaibocai.qcloud.service.CosService;

import java.util.List;

/**
 * @program: woaibocai-parent
 * @description: 博客前端由用户上传的图片
 * @author: woaibocai
 * @create: 2024-01-18 17:33
 **/
@Tag(name = "cos文件上传")
@RestController
@RequestMapping("/api/blog/cos")
public class BlogCosController {
    @Resource
    private CosService cosService;

    @Operation(summary = "远程调用:用户上传图片")
    @PostMapping("auth/userUpload")
    public List<String> userUpload(List<MultipartFile> files) {
        return cosService.userUpload(files);
    }
}
