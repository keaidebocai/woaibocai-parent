package top.woaibocai.manager.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import top.woaibocai.model.dto.manager.category.QueryCategoryDto;
import top.woaibocai.model.dto.manager.category.UpdateCategoryDto;
import top.woaibocai.model.entity.blog.Category;
import top.woaibocai.model.others.manager.CategoryBySelector;

import java.util.List;

@Mapper
public interface CategoryMapper {
    IPage<Category> queryList(@Param("iPage") IPage<Category> iPage, @Param("queryCategoryDto") QueryCategoryDto queryCategoryDto);

    void deletedById(String id);

    void putOfCategory(UpdateCategoryDto updateCategoryDto);

    void insertCategory(UpdateCategoryDto updateCategoryDto);

    List<CategoryBySelector> categorySelector();
}
