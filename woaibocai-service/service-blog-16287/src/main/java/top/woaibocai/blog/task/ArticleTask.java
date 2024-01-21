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
import top.woaibocai.blog.mapper.MyCommentMapper;
import top.woaibocai.blog.service.FetchDateUtilService;
import top.woaibocai.model.Do.KeyValue;
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
    @Resource
    private MyCommentMapper myCommentMapper;
    // 每小时的 5n 分钟 同步一次浏览量
    @Scheduled(cron = "0 0,5,10,15,20,25,30,35,40,45,50,55 * * * ? ")
    @Transactional
    @PostConstruct
    public void synchronizedViews(){
        // 获取所有 文章id
        List<Object> values = hashOperationSSO.values(RedisKeyEnum.BLOG_FETCHDATE_ARTICLE_AND_URL);
        // 为什么要大费周章的 把Set 转为 List ？ 是因为 Set 老在 for (String key : keys) init bean 失败
        List<String> articleUrls = new ArrayList<>();
        if (values.isEmpty()) {
            Map<String, String> map = fetchDateUtilService.getArticleIdAndUrlMap();
            map.forEach((key,value) -> articleUrls.add(value));
        }
        values.forEach(value -> articleUrls.add((String) value));
        // 查询 redis 所有文章的 浏览量
        List<Article> list = new ArrayList<>();
        redisTemplate.executePipelined((RedisCallback<Void>) connection -> {
            articleUrls.forEach(url -> {
                Object viewCount = hashOperationSSO.get(RedisKeyEnum.BLOG_ARTICLE.articleUrl(url), "viewCount");
                // 如果文章不润在那就初始化他！
                if (viewCount == null ) {
                    BlogArticleVo articleVoByUrl = fetchDateUtilService.getArticleVoByUrl(url);
                    Article article = new Article();
                    article.setUrl(url);
                    article.setViewCount(articleVoByUrl.getViewCount());
                    list.add(article);
                } else {
                    Article article = new Article();
                    article.setUrl(url);
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
    @Scheduled(cron = "0 0 3 * * ? ")
    @Transactional
    @PostConstruct
    public void synchronizeComemntLike() {
        Map<String, Object> entries = hashOperationSSO.entries(RedisKeyEnum.BLOG_COMMENT_LIKE);
        if (!entries.isEmpty()) {
            List<KeyValue<String,Integer>> commentOfLikeCount = new ArrayList<>();
            entries.forEach((key,value) -> commentOfLikeCount.add(new KeyValue<>(key,(Integer) value)));
            myCommentMapper.synchronizeComemntLike(commentOfLikeCount);
            System.out.println("==============================点赞同步成功==============================");
        }
    }
}
