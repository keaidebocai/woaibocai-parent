package top.woaibocai.common.feign;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import top.woaibocai.model.common.Result;

import java.io.IOException;
import java.util.List;

@FeignClient(value = "woaibocai-service-qcloud-16288")
public interface QCloudFeignClint {
    @Operation(summary = "远程调用: 用户批量上传图片 userUpload")
    @PostMapping(value = "/api/blog/cos/userUpload",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public List<String> userUpload(@RequestPart List<MultipartFile> files) throws IOException;
    @Operation(summary = "远程调用: 用户上传头像 uploadThumbnail")
    @PostMapping(value = "bcblog/cos/uploadThumbnail",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Result userUploadAvatar(@RequestPart MultipartFile file) throws IOException;
}
