package top.woaibocai.blog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(scanBasePackages = {"top.woaibocai.blog","top.woaibocai.common"})
public class ServiceBlogApplication16287 {
    public static void main(String[] args) {
        SpringApplication.run(ServiceBlogApplication16287.class,args);
    }
}
