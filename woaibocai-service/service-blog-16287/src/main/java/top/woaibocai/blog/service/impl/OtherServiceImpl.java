package top.woaibocai.blog.service.impl;

import com.alibaba.druid.util.StringUtils;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import top.woaibocai.blog.mapper.ArticleMapper;
import top.woaibocai.blog.mapper.CategoryMapper;
import top.woaibocai.blog.mapper.TagMapper;
import top.woaibocai.blog.service.FetchDateUtilService;
import top.woaibocai.blog.service.OtherService;
import top.woaibocai.model.common.RedisKeyEnum;
import top.woaibocai.model.common.Result;
import top.woaibocai.model.common.ResultCodeEnum;
import top.woaibocai.model.vo.blog.other.RSSVo;
import top.woaibocai.model.vo.blog.other.Sitemap;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @program: woaibocai-parent
 * @description: 杂七杂八
 * @author: woaibocai
 * @create: 2024-01-24 19:13
 **/
@Service("OtherServiceImpl")
public class OtherServiceImpl implements OtherService {
    @Resource
    private ArticleMapper articleMapper;
    @Resource
    private CategoryMapper categoryMapper;
    @Resource
    private TagMapper tagMapper;
    @Resource
    private RedisTemplate<String,String> redisTemplateString;
    @Resource
    private RedisTemplate<String,Sitemap> redisTemplateObject;
    @Resource
    private RedisTemplate<String,RSSVo> redisTemplateOtherObject;
    @Resource
    private FetchDateUtilService fetchDateUtilService;
    @Override
    public Result<String> about() {
        String s = redisTemplateString.opsForValue().get(RedisKeyEnum.BLOG_ABOUT);
        if (StringUtils.isEmpty(s)) {
            String id = "likebocaiabout114514";
            String content = articleMapper.about(id);
            redisTemplateString.opsForValue().set(RedisKeyEnum.BLOG_ABOUT,content);
            return Result.build(content, ResultCodeEnum.SUCCESS);
        }
        return Result.build(s,ResultCodeEnum.SUCCESS);
    }

    @Override
    public Result<String> link() {
        String s = redisTemplateString.opsForValue().get(RedisKeyEnum.BLOG_LINK);
        if (StringUtils.isEmpty(s)) {
            String id = "likebocaifriends114514";
            String content = articleMapper.about(id);
            redisTemplateString.opsForValue().set(RedisKeyEnum.BLOG_LINK,content);
            return Result.build(content, ResultCodeEnum.SUCCESS);
        }
        return Result.build(s,ResultCodeEnum.SUCCESS);
    }

    @Override
    public List<Sitemap> sitemap() {
        List<Sitemap> range = redisTemplateObject.opsForList().range(RedisKeyEnum.BLOG_SITEMAP, 0, -1);
        if (range.isEmpty()) {
            List<Sitemap> article = articleMapper.selectArticleUrlAndTime();
            List<Sitemap> category = categoryMapper.selectCategoryUrlAndTime();
            List<Sitemap> tag = tagMapper.selectTagUrlAndTime();
            List<Sitemap> sitemaps = new ArrayList<>();
            article.forEach(a -> {
                a.setLoc("article/" + a.getLoc());
                sitemaps.add(a);
            });
            category.forEach(c -> {
                c.setLoc("category/" + c.getLoc());
                sitemaps.add(c);
            });
            tag.forEach(t -> {
                t.setLoc("tag/" + t.getLoc());
                sitemaps.add(t);
            });
            redisTemplateObject.opsForList().rightPushAll(RedisKeyEnum.BLOG_SITEMAP,sitemaps);
            return sitemaps;
        }
        return range;
    }

    @Override
    public List<RSSVo> RSS() {
        List<RSSVo> range = redisTemplateOtherObject.opsForList().range(RedisKeyEnum.BLOG_RSS, 0, -1);
        if (range.isEmpty()) {
            List<RSSVo> rssVos = articleMapper.selectRSSOfArticle();
            Map<String,List<String>> urlOfTags = fetchDateUtilService.getAllUrlOfTags();
            rssVos.forEach(rssVo -> rssVo.setTags(urlOfTags.get(rssVo.getUrl())));
            redisTemplateOtherObject.opsForList().rightPushAll(RedisKeyEnum.BLOG_RSS,rssVos);
            return rssVos;
        }
        return range;
    }
}
