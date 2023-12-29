package top.woaibocai.manager.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import top.woaibocai.model.dto.manager.article.QueryArticleCriteria;
import top.woaibocai.model.dto.manager.article.UpdateArticleStatusDto;
import top.woaibocai.model.dto.manager.article.WriteArticleDto;
import top.woaibocai.model.entity.blog.Article;
import top.woaibocai.model.vo.manager.ArticleStatusPageVo;

@Mapper
public interface ArticleMapper extends BaseMapper<Article> {
    IPage<ArticleStatusPageVo> findPage(@Param("page") IPage<ArticleStatusPageVo> page, @Param("queryArticleCriteria") QueryArticleCriteria queryArticleCriteria);

    void updateArticleStatus(UpdateArticleStatusDto updateArticleStatusDto);

    void deletedArticleById(Integer id);

    void writeArticle(WriteArticleDto writeArticleDto);

    Article mySelectById(String id);

    void updateArticle(WriteArticleDto writeArticleDto);
}
