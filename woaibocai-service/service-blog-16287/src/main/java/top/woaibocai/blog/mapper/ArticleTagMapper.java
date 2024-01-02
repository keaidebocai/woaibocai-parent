package top.woaibocai.blog.mapper;

import org.apache.ibatis.annotations.Mapper;
import top.woaibocai.model.Do.blog.ArticleHasTagsDo;
import top.woaibocai.model.Do.blog.TagHasArticleCountDo;
import top.woaibocai.model.vo.blog.tag.TagInfo;

import java.util.List;

@Mapper
public interface ArticleTagMapper {
    List<TagHasArticleCountDo> tagByArticleCount();

    List<ArticleHasTagsDo> articleHasTags();

    List<String> articleHasTag(String id);

    List<TagInfo> TagIdTagNameByArticleId(String id);
}
