package top.woaibocai.qcloud.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface CosService {
    String uploadThumbnail(MultipartFile file) throws IOException;
}
