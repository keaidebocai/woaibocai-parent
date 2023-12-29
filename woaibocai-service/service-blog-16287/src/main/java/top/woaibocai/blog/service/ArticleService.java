package top.woaibocai.blog.service;

import top.woaibocai.model.common.Result;
import top.woaibocai.model.vo.blog.article.BlogArticleVo;

import java.util.List;

public interface ArticleService {
    Result<List<BlogArticleVo>> indexArticlePage(Integer current, Integer size);
    //初始化数据
    List<BlogArticleVo> fetchBlogArticlePageVoData();
}
