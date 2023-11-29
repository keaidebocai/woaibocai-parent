package top.woaibocai.qcloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@ComponentScan(basePackages = {"top.woaibocai"})
public class ServiceQcloudApplication16288 {
    public static void main(String[] args) {
        SpringApplication.run(ServiceQcloudApplication16288.class,args);
    }
}