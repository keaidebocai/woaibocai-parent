package top.woaibocai.blog.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BlogInfoMapper {
    List<Long> selectArticleView();

    Long selectTagsCount();

    Long categoryCount();
}
