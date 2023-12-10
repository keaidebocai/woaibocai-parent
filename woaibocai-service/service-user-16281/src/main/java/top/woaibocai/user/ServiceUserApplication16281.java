package top.woaibocai.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @program: woaibocai-parent
 * @description: 这里是关于一切用户所交互的接口 | 比如 评论接口 、 登录 、 注册 .....
 * @author: woaibocai
 * @create: 2023-12-09 14:59
 **/
@SpringBootApplication(scanBasePackages = {"top.woaibocai.user","top.woaibocai.common"})
@ComponentScan(basePackages = {"top.woaibocai"})
public class ServiceUserApplication16281 {
    public static void main(String[] args) {
        SpringApplication.run(ServiceUserApplication16281.class,args);
    }
}
