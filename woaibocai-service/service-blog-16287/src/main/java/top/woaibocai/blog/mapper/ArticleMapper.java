package top.woaibocai.blog.mapper;

import org.apache.ibatis.annotations.Mapper;
import top.woaibocai.model.entity.blog.Article;
import top.woaibocai.model.vo.blog.article.BlogArticleVo;

import java.util.List;

@Mapper
public interface ArticleMapper {
    List<BlogArticleVo> selectAllArticle();

    List<Article> seleAllViewCount();

    void updateView(List<Article> list);

    List<Article> getArticleIdAndUrlMap();

    BlogArticleVo selectArticleByUrl(String url);

    List<String> selectAllArticleUrl();

    List<String> selectArticleUrlByTagUrl(String tagUrl);
}
