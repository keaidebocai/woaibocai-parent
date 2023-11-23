package top.woaibocai.manager.service;

import top.woaibocai.model.common.Result;
import top.woaibocai.model.dto.manager.category.QueryCategoryDto;
import top.woaibocai.model.dto.manager.category.UpdateCategoryDto;

public interface CategoryService {
    Result list(Integer current, Integer size, QueryCategoryDto queryCategoryDto);

    Result deletedById(String id);

    Result putOfCategory(UpdateCategoryDto updateCategoryDto);
}
