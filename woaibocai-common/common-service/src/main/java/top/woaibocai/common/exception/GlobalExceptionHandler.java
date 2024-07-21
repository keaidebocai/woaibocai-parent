package top.woaibocai.common.exception;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import top.woaibocai.model.common.Result;

import java.util.List;
import java.util.stream.Collectors;


/**
 * @program: bczx-parent
 * @description:
 * @author: woaibocai
 * @create: 2023-10-19 17:24
 **/
@RestControllerAdvice
public class GlobalExceptionHandler {

//    @ExceptionHandler(Exception.class)
//    public Result error(){
//        return Result.build(null, ResultCodeEnum.SYSTEM_ERROR);
//    }

    //自定义异常处理
    @ExceptionHandler(BoCaiException.class)
    public Result error(BoCaiException e){
        return Result.build(null, e.getResultCodeEnum());
    }
    @ExceptionHandler({MethodArgumentNotValidException.class, BindException.class})
    public Result<?> handlerValidException(BindException e){
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        String message = fieldErrors.stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.joining(";"));
        return Result.build(null,400,message);
    }

}
