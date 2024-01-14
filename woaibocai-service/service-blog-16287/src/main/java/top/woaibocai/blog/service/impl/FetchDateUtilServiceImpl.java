package top.woaibocai.blog.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.stereotype.Service;
import top.woaibocai.blog.mapper.ArticleMapper;
import top.woaibocai.blog.mapper.ArticleTagMapper;
import top.woaibocai.blog.mapper.BlogInfoMapper;
import top.woaibocai.blog.mapper.MyCommentMapper;
import top.woaibocai.blog.service.FetchDateUtilService;
import top.woaibocai.model.Do.KeyAndValue;
import top.woaibocai.model.Do.blog.TagHasArticleCountDo;
import top.woaibocai.model.common.RedisKeyEnum;
import top.woaibocai.model.entity.blog.Article;
import top.woaibocai.model.vo.blog.BlogInfoVo;
import top.woaibocai.model.vo.blog.article.BlogArticleVo;
import top.woaibocai.model.vo.blog.comment.CommentDataVo;
import top.woaibocai.model.vo.blog.comment.OneCommentVo;
import top.woaibocai.model.vo.blog.tag.TagInfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @program: woaibocai-parent
 * @description: 获取数据的初始化工具实现类
 * @author: woaibocai
 * @create: 2023-12-30 17:05
 **/
@Service
public class FetchDateUtilServiceImpl implements FetchDateUtilService {
    @Resource
    private ArticleMapper articleMapper;
    @Resource
    private ArticleTagMapper articleTagMapper;
    @Resource
    private BlogInfoMapper blogInfoMapper;
    @Resource
    private MyCommentMapper myCommentMapper;
    @Resource
    private HashOperations<String,String,String> hashOperationSSS ;
    @Resource
    private HashOperations<String,String,Integer> hashOperationsSSI ;
    @Resource
    private HashOperations<String,String,Object> hashOperationSSO;
    @Resource
    private ListOperations<String,String> listOperationSS;
    @Resource
    private ObjectMapper objectMapper;
    @Override
    public Map<String, String> getArticleIdAndUrlMap() {
        // 查询 redis
        Map<String, String> entries = hashOperationSSS.entries(RedisKeyEnum.BLOG_FETCHDATE_ARTICLE_AND_URL);
        // 没有
        if (entries.isEmpty()) {
            List<Article> articleIdAndUrlList = articleMapper.getArticleIdAndUrlMap();
            Map<String, String> map = new HashMap<>();
            for (Article article : articleIdAndUrlList) {
                map.put(article.getId(),article.getUrl());
            }
            hashOperationSSS.putAll(RedisKeyEnum.BLOG_FETCHDATE_ARTICLE_AND_URL,map);
            return map;
        }
        // 有
        return entries;
    }

    // 调用者无法再redis上查询到文章
    // 功能 ： 初始化文章 并获取文章
    // 目前调用函数 1 个 且调用者已经判断 文章 id 真实存在
    // 所以不用判断 文章 id 是否存在
    @Override
    public BlogArticleVo getArticleVoByUrl(String url) {
        // 拿着 id 查询数据库拼接数据 然后推上 redis 并返回数据
        // 1.联表查询所有 没有删除的文章 把用户id userId 转成用户名 userName 把文章分类转成文章分类名 blogCategoryName
        BlogArticleVo blogArticleVo = articleMapper.selectArticleByUrl(url);
        // 2.操作 赋值 setTags()
        // 用 tagHasArtilceCountMap 每个标签有多少文章 和 tagIdNameMap 所有标签和标签id 该文章的所有标签 List<String> articleHasTags
        // 获取 tagHasArtilceCountMap
        Map<String,Integer> tagHasArtilceCountMap = getTagHasArtilceCountMap();
        List<TagInfo> tags = articleTagMapper.TagIdTagNameByArticleId(blogArticleVo.getId());
        StringBuffer keywords = new StringBuffer("菠菜的小窝");
        for (TagInfo tagInfo : tags ) {
            tagInfo.setThisTagHasArticleCount(tagHasArtilceCountMap.get(tagInfo.getId()));
            tagInfo.setId("114514");
            keywords.append("," + tagInfo.getTagName());
        }
        blogArticleVo.setTags(tags);
        blogArticleVo.setKeywords(keywords.toString());
        // 3.赋值 articleLength readingDuration
        int articleLength = blogArticleVo.getContent().length();
        blogArticleVo.setArticleLength(articleLength);
        blogArticleVo.setReadingDuration(articleLength / 300);

        // 4. 把 文章推上 redis 初始化数据
        // 把 blogArticleVo 转成 map
        Map<String,Object>  blogArticleVoMap = objectMapper.convertValue(blogArticleVo, Map.class);
        // 要根据 文章 id 查询评论 暂时暴露id
//        blogArticleVoMap.remove("id");
        hashOperationSSO.putAll(RedisKeyEnum.BLOG_ARTICLE.articleUrl(url),blogArticleVoMap);

        // 5.返回 blogArticleVo
        return blogArticleVo;
    }

    @Override
    public Map<String, Integer> tagHasArtilceCountMap() {
        return getTagHasArtilceCountMap();
    }

    @Override
    public BlogInfoVo getBlogInfo() {
        // 文章数
        Long articleSize = hashOperationSSS.size(RedisKeyEnum.BLOG_FETCHDATE_BLOG_INFO);
        if (articleSize == 0) {
            Map<String, String> articleIdAndUrlMap = this.getArticleIdAndUrlMap();
            articleSize = (long) articleIdAndUrlMap.size();
        }
        // 标签数
        Long tagSize = hashOperationSSS.size(RedisKeyEnum.BLOG_FETCHDATE_TAG_HAS_ARTICLE_COUNT_MAP);
        if (tagSize == 0) {
            tagSize = (long) this.tagHasArtilceCountMap().size();
        }
        // 分类数
        Long categoryCount = blogInfoMapper.categoryCount();
        // 总浏览量
        List<Long> articleViewList = blogInfoMapper.selectArticleView();
        Long viewCount = 0L;
        for ( Long view : articleViewList ) {
            viewCount += view;
        }
        BlogInfoVo blogInfoVo = new BlogInfoVo();
        blogInfoVo.setArticleCount(articleSize);
        blogInfoVo.setArticleViewCount(viewCount);
        blogInfoVo.setTagCount(tagSize);
        blogInfoVo.setCategoryCount(categoryCount);
        Map map = objectMapper.convertValue(blogInfoVo, Map.class);
        // 推上redis
        hashOperationSSO.putAll(RedisKeyEnum.BLOG_FETCHDATE_BLOG_INFO,map);
        return blogInfoVo;
    }
    // 初始化 该文章的 一级评论列表
    @Override
    public List<String> initOneCommentList(String articleId) {
        // 根据 文章id 查询此文章的所有一级评论
        List<String> oneCommentList = myCommentMapper.selectOneCommentListByArticleId(articleId);
        // 不管有没有 都 给他设置 oneCommentList,如果是空那就相当于设置key，如果不为空那就正常初始化值
        listOperationSS.rightPushAll(RedisKeyEnum.BLOG_COMMENT_ARTICLE.articleUrl(articleId),oneCommentList);
        return oneCommentList;
    }

    @Override
    public Long getThisArticleCommentTotal(String articleId) {
        // 获取该文章的所有评论总数
        Integer total = hashOperationsSSI.get(RedisKeyEnum.BLOG_COMMENT_COUNT, articleId);
        System.out.println(total);
         // 是null 证明key不存在要初始化整个hash
        if (total == null) {
            // 查询数据库 更新所有文章的总评论数
            List<KeyAndValue> allCommentTotal = myCommentMapper.getAllArticleCommentTotal();
            Map<String,Integer> map = new HashMap<>();
            allCommentTotal.forEach(kv -> map.put(kv.getMyKey(), Integer.valueOf(kv.getMyValue())));
            hashOperationsSSI.putAll(RedisKeyEnum.BLOG_COMMENT_COUNT,map);
            if (!map.containsKey(articleId)) {
                return 0L;
            }
            System.out.println(map.get(articleId));
            return map.get(articleId).longValue();
        }
        return total.longValue();
    }

    @Override
    public OneCommentVo initOneComment(String oneCommentId) {
        OneCommentVo oneCommentVo = myCommentMapper.selectOneCommentById(oneCommentId);
        // 不用判空，因为在列表的评论都真实存在
        KeyAndValue keyAndValue = myCommentMapper.selectUserNameAndAvaterByUserId(oneCommentVo.getSendId());
        oneCommentVo.setSendUserAvater(keyAndValue.getMyKey());
        oneCommentVo.setSandUserNickName(keyAndValue.getMyValue());
        Map<String,Object> map = objectMapper.convertValue(oneCommentVo, Map.class);
        // 初始化redis
        hashOperationSSO.putAll(RedisKeyEnum.BLOG_COMMENT_ALL.comment(oneCommentId),map);
        return oneCommentVo;
    }

    @Override
    public List<String> initTwoCommentListByOneId(String oneCommentId) {
        List<String> list = myCommentMapper.selectTwoCommentIdByOneId(oneCommentId);
        if (list.isEmpty()) {
            return list;
        }
        listOperationSS.rightPushAll(RedisKeyEnum.BLOG_COMMENT_ONECOMMENT.comment(oneCommentId),list);
        return list;
    }

    @Override
    public CommentDataVo initTwoCommentById(String id) {
        CommentDataVo commentDataVo = myCommentMapper.selectCommentDataVo(id);
        KeyAndValue send = myCommentMapper.selectUserNameAndAvaterByUserId(commentDataVo.getSendId());
        KeyAndValue reply = myCommentMapper.selectUserNameAndAvaterByUserId(commentDataVo.getReplyId());
        commentDataVo.setSendUserAvater(send.getMyKey());
        commentDataVo.setSendUserNickName(send.getMyValue());
        commentDataVo.setReplyUserAvater(reply.getMyKey());
        commentDataVo.setSendUserNickName(reply.getMyValue());
        Map<String,Object> map = objectMapper.convertValue(commentDataVo, Map.class);
        hashOperationSSO.putAll(RedisKeyEnum.BLOG_COMMENT_ALL.comment(id),map);
        return commentDataVo;
    }

    private Map<String, Integer> getTagHasArtilceCountMap() {
        // 从 redis 上 key "blog:fetchDate:tagHasArtilceCountMap" 获取 tagHasArtilceCountMap
        Map<String, Integer> tagHasArtilceCountMap = hashOperationsSSI.entries(RedisKeyEnum.BLOG_FETCHDATE_TAG_HAS_ARTICLE_COUNT_MAP);
        // 判空
        if (!tagHasArtilceCountMap.isEmpty()) {
            return tagHasArtilceCountMap;
        }
        // 空 查数据库 推上redis 返回数据
        // tagHasArticleCountDoList 空就报错，自己抛出异常 懒！！！
        List<TagHasArticleCountDo> tagHasArticleCountDoList = articleTagMapper.tagByArticleCount();
        Map<String,Integer> map = new HashMap<>();
        for (TagHasArticleCountDo item : tagHasArticleCountDoList) {
            map.put(item.getTagId(), item.getArticleCount());
        }
        hashOperationsSSI.putAll(RedisKeyEnum.BLOG_FETCHDATE_TAG_HAS_ARTICLE_COUNT_MAP,map);
        return map;
    }
}
