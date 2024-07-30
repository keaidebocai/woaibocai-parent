package top.woaibocai.model.vo.email;

import lombok.Data;

import java.util.List;

/**
 * @program: woaibocai-parent
 * @description: 分页返回类
 * @author: LikeBocai
 * @create: 2024/7/22 15:21
 **/
@Data
public class LikebocaiPageVo<T> {

    private List<T> pageData;

    private int total;

    private int current;

    private int size;
}
