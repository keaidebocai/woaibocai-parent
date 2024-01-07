package top.woaibocai.blog.service;

import top.woaibocai.model.common.Result;
import top.woaibocai.model.vo.blog.category.CategoryUrlNameIconVo;

import java.util.List;

public interface HeaderService {
    Result<List<CategoryUrlNameIconVo>> getAllCategoryNameAndUrl();

}
