package top.woaibocai.blog.service;

import top.woaibocai.model.common.Result;
import top.woaibocai.model.vo.blog.article.BlogArticlePageVo;

import java.util.List;

public interface ArticleService {
    Result<List<BlogArticlePageVo>> indexArticlePage(Integer current, Integer size);
}
