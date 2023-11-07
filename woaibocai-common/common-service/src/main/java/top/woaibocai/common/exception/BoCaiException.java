package top.woaibocai.common.exception;

import lombok.Data;
import top.woaibocai.model.common.ResultCodeEnum;


/**
 * @program: bczx-parent
 * @description: 菠菜把异常扔给你了！
 * @author: woaibocai
 * @create: 2023-10-19 17:28
 **/
@Data
public class BoCaiException extends RuntimeException{
    private Integer code;
    private String message;
    private ResultCodeEnum resultCodeEnum;

    public BoCaiException(ResultCodeEnum resultCodeEnum){
        this.resultCodeEnum = resultCodeEnum;
        this.code = resultCodeEnum.getCode();
        this.message = resultCodeEnum.getMessage();
    }
}
