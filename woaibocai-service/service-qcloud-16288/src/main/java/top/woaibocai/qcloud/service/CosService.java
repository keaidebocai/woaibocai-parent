package top.woaibocai.qcloud.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface CosService {
    String uploadThumbnail(MultipartFile file) throws IOException;

    List<String> userUpload(List<MultipartFile> files) throws IOException;
}
