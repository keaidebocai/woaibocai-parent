package top.woaibocai.manager.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import top.woaibocai.model.common.Result;
import top.woaibocai.model.dto.manager.article.QueryArticleCriteria;
import top.woaibocai.model.dto.manager.article.UpdateArticleStatusDto;
import top.woaibocai.model.vo.manager.ArticlePageVo;

public interface ArticleService {
    Result<IPage<ArticlePageVo>> findPage(Integer current, Integer size, QueryArticleCriteria queryArticleCriteria);

    void updateArticleStatus(UpdateArticleStatusDto updateArticleStatusDto);

    void deletedArticleById(Integer id);
}
