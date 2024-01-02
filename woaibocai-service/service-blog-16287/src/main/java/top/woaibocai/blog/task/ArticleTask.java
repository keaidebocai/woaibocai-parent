package top.woaibocai.blog.task;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;
import top.woaibocai.blog.mapper.ArticleMapper;
import top.woaibocai.blog.service.FetchDateUtilService;
import top.woaibocai.model.common.RedisKeyEnum;
import top.woaibocai.model.entity.blog.Article;
import top.woaibocai.model.vo.blog.BlogInfoVo;
import top.woaibocai.model.vo.blog.article.BlogArticleVo;

import java.util.*;

/**
 * @program: woaibocai-parent
 * @description: 文章相关的定时任务
 * @author: woaibocai
 * @create: 2023-12-29 17:33
 **/
@Configuration
@EnableScheduling
public class ArticleTask {
    @Resource
    private HashOperations<String,String,Object> hashOperationSSO;
    @Resource
    private RedisTemplate<String,Object> redisTemplate;
    @Resource
    private ArticleMapper articleMapper;
    @Resource
    private FetchDateUtilService fetchDateUtilService;
    // 每小时的 5n 分钟 同步一次浏览量
    @Scheduled(cron = "0 0,5,10,15,20,25,30,35,40,45,50,55 * * * ? ")
    @Transactional
    @PostConstruct
    public void synchronizedViews(){
        // 获取所有 文章id
        Set<String> keys = hashOperationSSO.keys(RedisKeyEnum.BLOG_FETCHDATE_ARTICLE_AND_URL);
        // 为什么要大费周章的 把Set 转为 List ？ 是因为 Set 老在 for (String key : keys) init bean 失败
        List<String> articleIds = new ArrayList<>();
        if (keys.isEmpty()) {
            Map<String, String> map = fetchDateUtilService.getArticleIdAndUrlMap();
            map.forEach((key,value) -> articleIds.add(key));
        }
        articleIds.addAll(keys);
        // 查询 redis 所有文章的 浏览量
        List<Article> list = new ArrayList<>();
        redisTemplate.executePipelined((RedisCallback<Void>) connection -> {
            articleIds.forEach(key -> {
                Object viewCount = hashOperationSSO.get(RedisKeyEnum.BLOG_ARTICLE.articleId(key), "viewCount");
                // 如果文章不润在那就初始化他！
                if (viewCount == null ) {
                    BlogArticleVo articleVoById = fetchDateUtilService.getArticleVoById(key);
                    Article article = new Article();
                    article.setId(key);
                    article.setViewCount(articleVoById.getViewCount());
                    list.add(article);
                } else {
                    Article article = new Article();
                    article.setId(key);
                    article.setViewCount(Long.valueOf(viewCount.toString()));
                    list.add(article);
                }
            });
            return null;
        });
        articleMapper.updateView(list);

        // 同步所有文章的浏览量
        Long viewTotal = 0L;
        for (Article article : list ) {
            viewTotal += article.getViewCount();
        }

        // 判空
        Boolean hasKey = hashOperationSSO.hasKey(RedisKeyEnum.BLOG_FETCHDATE_BLOG_INFO, "articleViewCount");
        if (!hasKey) {
            BlogInfoVo blogInfo = fetchDateUtilService.getBlogInfo();
            hashOperationSSO.put(RedisKeyEnum.BLOG_FETCHDATE_BLOG_INFO,"articleViewCount",viewTotal);
        }

        // 更新 "blog:fetchDate:blogInfo" 的 articleViewCount
        hashOperationSSO.put(RedisKeyEnum.BLOG_FETCHDATE_BLOG_INFO,"articleViewCount",viewTotal);
        System.out.println("================同步成功================");
    }
}
