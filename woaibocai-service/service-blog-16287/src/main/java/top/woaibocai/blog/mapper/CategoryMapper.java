package top.woaibocai.blog.mapper;

import org.apache.ibatis.annotations.Mapper;
import top.woaibocai.model.entity.blog.Category;
import top.woaibocai.model.vo.blog.category.CategoryUrlNameIconVo;
import top.woaibocai.model.vo.blog.other.Sitemap;

import java.util.List;

@Mapper
public interface CategoryMapper {
    Category categoryNameAnddeScription(String categoryUrl);

    List<CategoryUrlNameIconVo> categoryOfUrlNameIcon();

    List<Sitemap> selectCategoryUrlAndTime();
}
