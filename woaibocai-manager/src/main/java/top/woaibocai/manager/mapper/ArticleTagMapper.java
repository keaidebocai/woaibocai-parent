package top.woaibocai.manager.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import top.woaibocai.model.vo.manager.TagVo;

import java.util.List;

@Mapper
public interface ArticleTagMapper {
    void insertArticleTag(@Param("id") String id,@Param("tags") List<TagVo> tags);

    List<TagVo> selectByArticleId(String id);

    void deleteByArticleId(String id);
}
