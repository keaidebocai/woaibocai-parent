package top.woaibocai.manager.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import top.woaibocai.model.dto.manager.tag.QueryTagDto;
import top.woaibocai.model.entity.blog.Tag;



@Mapper
public interface TagMapper {
    IPage<Tag> findByTageName(@Param("iPage") IPage<Tag> iPage, @Param("queryTagDto") QueryTagDto queryTagDto);

    void insertTag(QueryTagDto queryTagDto);

    void deleted(String id);

    void updateTag(QueryTagDto queryTagDto);
}
