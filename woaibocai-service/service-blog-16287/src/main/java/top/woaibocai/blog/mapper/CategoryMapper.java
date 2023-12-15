package top.woaibocai.blog.mapper;

import org.apache.ibatis.annotations.Mapper;
import top.woaibocai.model.vo.blog.category.CategoryAllVo;

import java.util.List;

@Mapper
public interface CategoryMapper {
    List<CategoryAllVo> getAllCategory();
}
