package top.woaibocai.common.config;

import cn.hutool.core.util.RandomUtil;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springdoc.core.customizers.GlobalOpenApiCustomizer;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @program: woaibocai-parent
 * @description: Knife4jConfig配置类
 * @author: woaibocai
 * @create: 2023-11-07 14:17
 **/
@Configuration
public class Knife4jConfig {
////    @Bean
////    public GroupedOpenApi adminApi() {      // 创建了一个api接口的分组
////        return GroupedOpenApi.builder()
////                .group("default")         // 分组名称
////                .pathsToMatch("/**")  // 接口请求路径规则
////                .build();
////    }
//    /***
//     * @description 自定义接口信息
//     */
//    @Bean
//    public OpenAPI customOpenAPI() {
//
//        return new OpenAPI()
//                .info(new Info()
//                        .title("菠菜的小窝-API接口文档")
//                        .version("1.0")
//                        .description("菠菜的小窝-API接口文档")
//                        .contact(new Contact().name("woaibocai"))); // 设定作者
//    }
    /**
     * 根据@Tag 上的排序，写入x-order
     *
     * @return the global open api customizer
     */
    @Bean
    public GlobalOpenApiCustomizer orderGlobalOpenApiCustomizer() {
        return openApi -> {
            if (openApi.getTags()!=null){
                openApi.getTags().forEach(tag -> {
                    Map<String,Object> map=new HashMap<>();
                    map.put("x-order", RandomUtil.randomInt(0,100));
                    tag.setExtensions(map);
                });
            }
            if(openApi.getPaths()!=null){
                openApi.addExtension("x-test123","333");
                openApi.getPaths().addExtension("x-abb",RandomUtil.randomInt(1,100));
            }

        };
    }

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("菠菜的小窝API")
                        .version("1.0")

                        .description( "菠菜的小窝")
                        .termsOfService("http://www.likebocai.com")
                        .license(new License().name("Apache 2.0")
                                .url("http://www.likebocai.com")));
    }
}