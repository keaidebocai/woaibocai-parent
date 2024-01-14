package top.woaibocai.blog.service;

import top.woaibocai.model.vo.blog.BlogInfoVo;
import top.woaibocai.model.vo.blog.article.BlogArticleVo;

import java.util.Map;

public interface FetchDateUtilService {
    // 所有 文章id 和 url 并同步redis
    Map<String,String> getArticleIdAndUrlMap();

    // 初始化文章 并获取文章
    BlogArticleVo getArticleVoByUrl(String url);
    Map<String, Integer> tagHasArtilceCountMap();

    BlogInfoVo getBlogInfo();

    void initOneCommentList(String articleId);
}
