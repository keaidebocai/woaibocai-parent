package top.woaibocai.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @program: woaibocai-parent
 * @description: http请求/响应异常
 * @author: woaibocai
 * @create: 2023-11-14 08:17
 **/
@ResponseStatus(value = HttpStatus.UNAUTHORIZED,reason = "没有权限!")
public class HttpException extends RuntimeException{
}
