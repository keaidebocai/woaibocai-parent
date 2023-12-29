package top.woaibocai.blog.mapper;

import org.apache.ibatis.annotations.Mapper;
import top.woaibocai.model.vo.blog.article.BlogArticleVo;

import java.util.List;

@Mapper
public interface ArticleMapper {
    List<BlogArticleVo> selectAllArticle();
}
