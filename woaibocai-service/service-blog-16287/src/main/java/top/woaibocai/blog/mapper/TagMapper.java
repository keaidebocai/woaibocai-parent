package top.woaibocai.blog.mapper;

import org.apache.ibatis.annotations.Mapper;
import top.woaibocai.model.entity.blog.Tag;

import java.util.List;

@Mapper
public interface TagMapper {
    List<Tag> tagName();
}
