package top.woaibocai.blog.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import top.woaibocai.blog.mapper.ArticleMapper;
import top.woaibocai.blog.service.ArticleService;
import top.woaibocai.blog.service.FetchDateUtilService;
import top.woaibocai.model.common.RedisKeyEnum;
import top.woaibocai.model.common.Result;
import top.woaibocai.model.common.ResultCodeEnum;
import top.woaibocai.model.vo.blog.article.BlogArticleVo;

import java.util.*;

@Service
public class ArticleServiceImpl implements ArticleService {
    @Resource
    private ArticleMapper articleMapper;
    @Resource
    private ObjectMapper objectMapper;
    @Resource
    private RedisTemplate<String,Object> objectRedisTemplate;
    @Resource
    private ListOperations<String,String> listOperations;
    @Resource
    private FetchDateUtilService fetchDateUtilService;
    // hash 数据类型，为什么要在声明一个呢？用过用list的，那就牵扯到类型墙砖问题了，虽然不管怎么都要强转，但我选择重新声明再封装一下
    @Resource
    private HashOperations<String,String,Object> hashOperationSSO;
    @Override
    public Result<Map<String,Object>> indexArticlePage(Integer current, Integer size) {
        // 获取 list "blog:AllBlogArticlePageVo" 截取长度
        Long total = listOperations.size(RedisKeyEnum.BLOG_AERICLE_INDEX);
        // 说明他是空的，不存在 ！需要初始化！
        if (total == 0L) {
            List<String> articleUrlList = articleMapper.selectAllArticleUrl();
            total = listOperations.leftPushAll(RedisKeyEnum.BLOG_AERICLE_INDEX, articleUrlList);
            // 把数组倒过来
            Collections.reverse(articleUrlList);
            // 由于 subList 是 左闭右开 [....) 所以 不能 -1
            if (size * current > total.intValue()) {
                List<String> articleUrls = articleUrlList.subList((size * (current - 1)), total.intValue());
                Result<Map<String, Object>> blogArticleVoList = getBlogArticleVoList(articleUrls, current, size, Math.toIntExact(total));
                return blogArticleVoList;
            }
            List<String> articleUrls = articleUrlList.subList(size * (current - 1), current * size);
            Result<Map<String, Object>> blogArticleVoList = getBlogArticleVoList(articleUrls, current, size, Math.toIntExact(total));
            return blogArticleVoList;
        }

        long start = (long) size * (current -1);
        long end = (size * current) - 1;
        if (end > total - 1) {
            end = total - 1;
        }
        List<String> range = listOperations.range(RedisKeyEnum.BLOG_AERICLE_INDEX, start, end);
        Result<Map<String, Object>> blogArticleVoList = getBlogArticleVoList(range, current, size, Math.toIntExact(total));
        return blogArticleVoList;
    }

    private Result<Map<String,Object>> getBlogArticleVoList(List<String> articleUrls,Integer current,Integer size,Integer total) {
        List<BlogArticleVo> blogArticleVoList = new ArrayList<>();
        objectRedisTemplate.executePipelined((RedisCallback<Void>) connection -> {
            articleUrls.forEach(articleUrl -> {
                Map<String, Object> entries = hashOperationSSO.entries(RedisKeyEnum.BLOG_ARTICLE.articleUrl(articleUrl));
                if (entries.isEmpty()) {
                    BlogArticleVo articleVoByUrl = fetchDateUtilService.getArticleVoByUrl(articleUrl);
                    articleVoByUrl.setId(null);
                    articleVoByUrl.setContent(null);
                    blogArticleVoList.add(articleVoByUrl);
                } else {
                    entries.remove("content");
                    entries.remove("id");
                    BlogArticleVo blogArticleVo = objectMapper.convertValue(entries, BlogArticleVo.class);
                    blogArticleVoList.add(blogArticleVo);
                }
            });
            return null;
        });
        Map<String,Object> data = new HashMap<>();
        data.put("total",total);
        data.put("current",current);
        data.put("size",size);
        data.put("data",blogArticleVoList);

        // 增加总浏览量 +1
        hashOperationSSO.increment(RedisKeyEnum.BLOG_FETCHDATE_BLOG_INFO,"articleViewCount",1L);
        return Result.build(data,ResultCodeEnum.SUCCESS);
    }

    @Override
    public Result<BlogArticleVo> getArticleByUrl(String url) {
        // 查看 id是否存在
        // "blog:fetchDate:articleAndUrl" 如果不存在也会返回 false，所以直接获取他的所有，然后再判断是否存在haskey
        // 获取所有 文章id
        Map articleAndUrlMap = hashOperationSSO.entries(RedisKeyEnum.BLOG_FETCHDATE_ARTICLE_AND_URL);
        // 判断 articleAndUrlMap 是否存在
        // 不存在就从 fetchDateUtilService 获取一个
        if (articleAndUrlMap.isEmpty()) {
            Map map = fetchDateUtilService.getArticleIdAndUrlMap();
            // 判断 文章是否真是存在 存在就返回数据，不存在就 报数据异常
            return hasArticle(map, url);
        }
        // 存在 就再判断 文章是否真实存在
        return hasArticle(articleAndUrlMap,url);
    }

    @Override
    public Result<Map<String, Object>> articlePageBytagUrl(String tagUrl, Integer current, Integer size) {
        // 查看 redis 是否有关于 tagUrl 索引的总数量
        Long total = listOperations.size(RedisKeyEnum.BLOG_TAG_INDEX.tagUrl(tagUrl));
        // 判断 total 是否为空 空则查数据库 并 初始化 redis
        if (total == 0L) {
            List<String> articlesByTagUrl =  articleMapper.selectArticleUrlByTagUrl(tagUrl);
            if(articlesByTagUrl.isEmpty()) {
                return Result.build(null,ResultCodeEnum.DATA_ERROR);
            }
            total = listOperations.leftPushAll(RedisKeyEnum.BLOG_TAG_INDEX.tagUrl(tagUrl), articlesByTagUrl);
            Collections.reverse(articlesByTagUrl);
            // 由于 subList 是 左闭右开 [....) 所以 不能 -1
            if (size * current > total.intValue()) {
                List<String> articleUrls = articlesByTagUrl.subList((size * (current - 1)), total.intValue());
                Result<Map<String, Object>> blogArticleVoList = getBlogArticleVoList(articleUrls, current, size, Math.toIntExact(total));
                return blogArticleVoList;
            }
            List<String> articleUrls = articlesByTagUrl.subList(size * (current - 1), current * size);
            Result<Map<String, Object>> blogArticleVoList = getBlogArticleVoList(articleUrls, current, size, Math.toIntExact(total));
            return blogArticleVoList;

        }
        long start = (long) size * (current -1);
        long end = (size * current) - 1;
        if (end > total - 1) {
            end = total - 1;
        }
        List<String> range = listOperations.range(RedisKeyEnum.BLOG_TAG_INDEX.tagUrl(tagUrl), start, end);
        Result<Map<String, Object>> blogArticleVoList = getBlogArticleVoList(range, current, size, Math.toIntExact(total));
        return blogArticleVoList;
    }

    // 判断 文章是否真是存在 存在就返回数据，不存在就 报数据异常
    private Result<BlogArticleVo> hasArticle(Map articleAndUrlMap, String url) {
        if (!articleAndUrlMap.containsValue(url)) {
            return Result.build(null,ResultCodeEnum.DATA_ERROR);
        }
        // 存在就查询 redis 获取文章
        Map<String, Object> articleMap = hashOperationSSO.entries(RedisKeyEnum.BLOG_ARTICLE.articleUrl(url));
        // 如果文章是空的 那就初始化 redis一下
        if (articleMap.isEmpty()) {
            // 初始化文章 并获取文章
            BlogArticleVo blogArticleVo = fetchDateUtilService.getArticleVoByUrl(url);
            // 刷新 文章的 viewCount
            // +2 的原因是当这一次调用接口也是一次浏览，但是浏览的数据并没有上去
            hashOperationSSO.increment(RedisKeyEnum.BLOG_ARTICLE.articleUrl(url),"viewCount",2L);
            // 小细节 显示问题
            blogArticleVo.setViewCount(blogArticleVo.getViewCount()+1L);
            return Result.build(blogArticleVo,ResultCodeEnum.SUCCESS);
        }
        // 不是空的就转化为对象返回
        BlogArticleVo blogArticleVo = objectMapper.convertValue(articleMap, BlogArticleVo.class);
        hashOperationSSO.increment(RedisKeyEnum.BLOG_ARTICLE.articleUrl(url),"viewCount",1L);
        return Result.build(blogArticleVo,ResultCodeEnum.SUCCESS);

    }
    //
}
