package top.woaibocai.blog.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.stereotype.Service;
import top.woaibocai.blog.mapper.ArticleMapper;
import top.woaibocai.blog.mapper.ArticleTagMapper;
import top.woaibocai.blog.mapper.BlogInfoMapper;
import top.woaibocai.blog.service.FetchDateUtilService;
import top.woaibocai.model.Do.blog.TagHasArticleCountDo;
import top.woaibocai.model.entity.blog.Article;
import top.woaibocai.model.vo.blog.BlogInfoVo;
import top.woaibocai.model.vo.blog.article.BlogArticleVo;
import top.woaibocai.model.vo.blog.tag.TagInfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private HashOperations<String,String,String> hashOperationSSS ;
    @Resource
    private HashOperations<String,String,Integer> hashOperationsSSI ;
    @Resource
    private HashOperations<String,String,Object> hashOperationSSO;
    @Override
    public Map<String, String> getArticleIdAndUrlMap() {
        // 查询 redis
        Map<String, String> entries = hashOperationSSS.entries("blog:fetchDate:articleAndUrl");
        // 没有
        if (entries.isEmpty()) {
            List<Article> articleIdAndUrlList = articleMapper.getArticleIdAndUrlMap();
            Map<String, String> map = new HashMap<>();
            for (Article article : articleIdAndUrlList) {
                map.put(article.getId(),article.getUrl());
            }
            hashOperationSSS.putAll("blog:fetchDate:articleAndUrl",map);
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
    public BlogArticleVo getArticleVoById(String id) {
        // 拿着 id 查询数据库拼接数据 然后推上 redis 并返回数据
        // 1.联表查询所有 没有删除的文章 把用户id userId 转成用户名 userName 把文章分类转成文章分类名 blogCategoryName
        BlogArticleVo blogArticleVo = articleMapper.selectArticleById(id);
        // 2.操作 赋值 setTags()
        // 用 tagHasArtilceCountMap 每个标签有多少文章 和 tagIdNameMap 所有标签和标签id 该文章的所有标签 List<String> articleHasTags
        // 获取 tagHasArtilceCountMap
        Map<String,Integer> tagHasArtilceCountMap = getTagHasArtilceCountMap();
        List<TagInfo> tags = articleTagMapper.TagIdTagNameByArticleId(id);
        for (TagInfo tagInfo : tags ) {
            tagInfo.setThisTagHasArticleCount(tagHasArtilceCountMap.get(tagInfo.getId()));
        }
        blogArticleVo.setTags(tags);

        // 3.赋值 articleLength readingDuration
        int articleLength = blogArticleVo.getContent().length();
        blogArticleVo.setArticleLength(articleLength);
        blogArticleVo.setReadingDuration(articleLength / 300);

        // 4. 把 文章推上 redis 初始化数据
        // 把 blogArticleVo 转成 map
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String,Object>  blogArticleVoMap = objectMapper.convertValue(blogArticleVo, Map.class);
        hashOperationSSO.putAll("blog:article:" + id,blogArticleVoMap);

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
        Long articleSize = hashOperationSSS.size("blog:fetchDate:articleAndUrl");
        if (articleSize == 0) {
            Map<String, String> articleIdAndUrlMap = this.getArticleIdAndUrlMap();
            articleSize = (long) articleIdAndUrlMap.size();
        }
        // 标签数
        Long tagSize = hashOperationSSS.size("blog:fetchDate:tagHasArtilceCountMap");
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
        ObjectMapper objectMapper = new ObjectMapper();
        Map map = objectMapper.convertValue(blogInfoVo, Map.class);
        // 推上redis
        hashOperationSSO.putAll("blog:fetchDate:blogInfo",map);
        return blogInfoVo;
    }

    private Map<String, Integer> getTagHasArtilceCountMap() {
        // 从 redis 上 key "blog:fetchDate:tagHasArtilceCountMap" 获取 tagHasArtilceCountMap
        Map<String, Integer> tagHasArtilceCountMap = hashOperationsSSI.entries("blog:fetchDate:tagHasArtilceCountMap");
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
        hashOperationsSSI.putAll("blog:fetchDate:tagHasArtilceCountMap",map);
        return map;
    }
}
