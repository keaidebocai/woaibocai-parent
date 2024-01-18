package top.woaibocai.common.feign;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(value = "woaibocai-service-qcloud-16288")
public interface QCloudFeignClint {
    @Operation(summary = "远程调用: test")
    @GetMapping("bcblog/cos/test")
    public String getTestString();
}
