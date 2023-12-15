package top.woaibocai.blog.service;

import top.woaibocai.model.common.Result;
import top.woaibocai.model.vo.blog.category.CategoryAllVo;

import java.util.List;

public interface CategoryService {
    Result<List<CategoryAllVo>> getAllCategory();
}
