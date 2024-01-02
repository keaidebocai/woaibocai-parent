package top.woaibocai.blog.service;

import top.woaibocai.model.common.Result;
import top.woaibocai.model.vo.blog.article.BlogArticleVo;

import java.util.Map;

public interface ArticleService {
    Result<Map<String,Object>> indexArticlePage(Integer current, Integer size);

    Result<BlogArticleVo> getArticleById(String id);
}
