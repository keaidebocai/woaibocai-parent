package top.woaibocai.blog.service;

import top.woaibocai.model.vo.blog.BlogInfoVo;
import top.woaibocai.model.vo.blog.article.BlogArticleVo;
import top.woaibocai.model.vo.blog.comment.CommentDataVo;
import top.woaibocai.model.vo.blog.comment.OneCommentVo;

import java.util.List;
import java.util.Map;

public interface FetchDateUtilService {
    // 所有 文章id 和 url 并同步redis
    Map<String,String> getArticleIdAndUrlMap();

    // 初始化文章 并获取文章
    BlogArticleVo getArticleVoByUrl(String url);
    Map<String, Integer> tagHasArtilceCountMap();

    BlogInfoVo getBlogInfo();

    List<String> initOneCommentList(String articleId);

    Long getThisArticleCommentTotal(String articleId);

    OneCommentVo initOneComment(String oneCommentId);

    List<String> initTwoCommentListByOneId(String oneCommentId);

    CommentDataVo initTwoCommentById(String id);
}
