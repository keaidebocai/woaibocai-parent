package top.woaibocai.blog.mapper;

import org.apache.ibatis.annotations.Mapper;
import top.woaibocai.model.Do.blog.ArticleHasTagsDo;
import top.woaibocai.model.Do.blog.TagHasArticleCountDo;

import java.util.List;

@Mapper
public interface ArticleTagMapper {
    List<TagHasArticleCountDo> tagByArticleCount();

    List<ArticleHasTagsDo> articleHasTags();
}
