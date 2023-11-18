package top.woaibocai.manager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import top.woaibocai.manager.properties.UserProperties;

/**
 * @program: woaibocai-parent
 * @description: 后台管理模块16289
 * @author: woaibocai
 * @create: 2023-11-05 22:54
 **/
@SpringBootApplication
//允许yaml中可以被读取到的类
@EnableConfigurationProperties(value = {UserProperties.class})
public class ManagerApplication16289 {
    public static void main(String[] args) {
        SpringApplication.run(ManagerApplication16289.class,args);
    }
}
