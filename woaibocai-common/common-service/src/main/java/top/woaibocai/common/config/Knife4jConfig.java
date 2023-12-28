package top.woaibocai.common.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @program: woaibocai-parent
 * @description: Knife4jConfig配置类
 * @author: woaibocai
 * @create: 2023-11-07 14:17
 **/
@Configuration
public class Knife4jConfig {
    @Bean
    public GroupedOpenApi adminApi() {      // 创建了一个api接口的分组
        return GroupedOpenApi.builder()
                .group("user模块")         // 分组名称
                .pathsToMatch("/**")  // 接口请求路径规则
                .build();
    }
    /***
     * @description 自定义接口信息
     */
    @Bean
    public OpenAPI customOpenAPI() {

        return new OpenAPI()
                .info(new Info()
                        .title("菠菜的小窝-API接口文档")
                        .version("1.0")
                        .description("菠菜的小窝-API接口文档")
                        .contact(new Contact().name("woaibocai"))); // 设定作者
    }
}