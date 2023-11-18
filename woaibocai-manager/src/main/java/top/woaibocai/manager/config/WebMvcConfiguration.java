package top.woaibocai.manager.config;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import top.woaibocai.manager.interceptor.LoginAuthInterceptor;
import top.woaibocai.manager.properties.UserProperties;

/**
 * @program: woaibocai-parent
 * @description: 配置跨域或拦截器等...
 * @author: woaibocai
 * @create: 2023-11-07 15:35
 **/
@Component
public class WebMvcConfiguration implements WebMvcConfigurer {

    @Resource
    private LoginAuthInterceptor loginAuthInterceptor;
    @Resource
    private UserProperties userProperties;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginAuthInterceptor)
                //哪些路径不拦截
                .excludePathPatterns(userProperties.getNoAuthUrls())
                //哪些要拦截
                .addPathPatterns("/**");
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")      // 添加路径规则
                .allowCredentials(true)               // 是否允许在跨域的情况下传递Cookie
                .allowedOriginPatterns("*")           // 允许请求来源的域规则
                .allowedMethods("*")
                .allowedHeaders("*") ;
    }
}
