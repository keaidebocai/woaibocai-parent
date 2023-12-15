package top.woaibocai.blog.mapper;

import org.apache.ibatis.annotations.Mapper;
import top.woaibocai.model.entity.blog.Menu;

import java.util.List;

@Mapper
public interface MenuMapper {
    List<Menu> getAllMenu();
}
