package top.woaibocai.gateway.filter;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.nacos.api.utils.StringUtils;
import jakarta.annotation.Resource;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import top.woaibocai.model.common.Result;
import top.woaibocai.model.Enum.ResultCodeEnum;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

/**
 * @program: woaibocai-parent
 * @description: 全局过滤器, 主要过滤带"auht"路径的，需要带token的接口
 * @author: woaibocai
 * @create: 2023-12-14 09:37
 **/
@Component
public class AuthGlobalFilter implements GlobalFilter, Ordered {
    @Resource
    private RedisTemplate<String,String> redisTemplate;
    private AntPathMatcher antPathMatcher = new AntPathMatcher();
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        //获取当前路径
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();
        //判断当前路径是否满足 /api/**/auth/**,登陆验证
        if (antPathMatcher.match("/api/**/auth/**",path)) {
            //登陆验证
            if (!this.isToken(request)) {
                ServerHttpResponse response = exchange.getResponse();
                return out(response, ResultCodeEnum.LOGIN_NOLL);
            }
        }
        return chain.filter(exchange);
    }

    private Mono<Void> out(ServerHttpResponse response, ResultCodeEnum resultCodeEnum) {
        Result result = Result.build("请勿假冒身份/请登录",resultCodeEnum);
        byte[] bits = JSONObject.toJSONString(result).getBytes(StandardCharsets.UTF_8);
        DataBuffer buffer = response.bufferFactory().wrap(bits);
        //指定编码，否则会在浏览器中中文乱码
        response.getHeaders().add("Content-Type", "application/json;charset=UTF-8");
        return response.writeWith(Mono.just(buffer));
    }

    @Override
    public int getOrder() {
        return 0;
    }

    // 验证token是否合法
    private Boolean isToken(ServerHttpRequest request) {
        String token = "";
        List<String> authorization = request.getHeaders().get("Authorization");
        if (null != authorization) {
            token = authorization.get(0).replace("Bearer ", "");
        }
        if (!StringUtils.isEmpty(token)) {
            //没序列化？
//            redisTemplate.opsForValue().set("nmsl","where is my token?");
            Map<Object, Object> useInfo = redisTemplate.opsForHash().entries("user:token:" + token);
            if (useInfo.isEmpty()) {
                return false;
            }else {
                String path = request.getURI().getPath();
                if (!antPathMatcher.match("/api/user/auth/getUserInfo",path)) {
                    String userId = (String) useInfo.get("userId");
                    String headerOfId = request.getHeaders().get("114514").get(0);
                    if (!(userId == null | userId.equals(headerOfId) | !headerOfId.equals("undefined"))) {
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }
}
