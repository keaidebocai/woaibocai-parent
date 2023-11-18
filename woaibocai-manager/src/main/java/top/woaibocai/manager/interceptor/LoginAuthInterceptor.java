package top.woaibocai.manager.interceptor;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSON;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import top.woaibocai.common.exception.HttpException;
import top.woaibocai.common.utils.AuthContextUtil;
import top.woaibocai.model.common.User;

import java.util.concurrent.TimeUnit;
@Component
public class LoginAuthInterceptor implements HandlerInterceptor {
    @Resource
    private RedisTemplate<String,String> redisTemplate;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //1.获取请求方式
        //如果是options 遇见请求，直接放行
        String method = request.getMethod();
        if ("OPTIONS".equals(method)){
            return true;
        }
        //2.从请求头上获取token
        String authorization = request.getHeader("Authorization");
        //3.如果token为空，返回错误信息提示
        if (StringUtils.isEmpty(authorization)){
            throw new HttpException();
        }
        String token = authorization.replace("Bearer ", "");
        //4.如果token不为空，拿着token查询redis
        String userInfo = redisTemplate.opsForValue().get("user::token:" + token);
        //5.如果redis查不到数据，返回错误信息
        if (StringUtils.isEmpty(userInfo)){
            throw new HttpException();
        }
        //6.如果redis查询用户信息，把用户信息放在threadLocal
        User user = JSON.parseObject(userInfo, User.class);
        AuthContextUtil.set(user);
        //7.把redis用户信息数据更新过期时间
        redisTemplate.expire("user::token:" + token,30, TimeUnit.MINUTES);
        //放行
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        //threadLocal删除
        AuthContextUtil.remove();
    }
}
