package top.woaibocai.manager.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import top.woaibocai.model.common.Result;
import top.woaibocai.model.dto.manager.article.QueryArticleCriteria;
import top.woaibocai.model.dto.manager.article.UpdateArticleStatusDto;
import top.woaibocai.model.dto.manager.article.WriteArticleDto;
import top.woaibocai.model.vo.manager.ArticleStatusPageVo;

public interface ArticleService {
    Result<IPage<ArticleStatusPageVo>> findPage(Integer current, Integer size, QueryArticleCriteria queryArticleCriteria);

    void updateArticleStatus(UpdateArticleStatusDto updateArticleStatusDto);

    void deletedArticleById(String id,String url);

    Result writeArticle(WriteArticleDto writeArticleDto);

    Result updateArticleData(String id);

    Result updateArticle(WriteArticleDto writeArticleDto);
}
