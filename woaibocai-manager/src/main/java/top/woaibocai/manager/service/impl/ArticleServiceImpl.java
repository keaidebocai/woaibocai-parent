package top.woaibocai.manager.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import top.woaibocai.common.utils.BeanCopyUtils;
import top.woaibocai.manager.mapper.ArticleMapper;
import top.woaibocai.manager.mapper.ArticleTagMapper;
import top.woaibocai.manager.service.ArticleService;
import top.woaibocai.model.Enum.RedisKeyEnum;
import top.woaibocai.model.common.Result;
import top.woaibocai.model.Enum.ResultCodeEnum;
import top.woaibocai.model.dto.manager.article.QueryArticleCriteria;
import top.woaibocai.model.dto.manager.article.UpdateArticleStatusDto;
import top.woaibocai.model.dto.manager.article.WriteArticleDto;
import top.woaibocai.model.entity.blog.Article;
import top.woaibocai.model.vo.manager.ArticleStatusPageVo;
import top.woaibocai.model.vo.manager.TagVo;


import java.util.List;
import java.util.UUID;

/**
 * @program: woaibocai-parent
 * @description:
 * @author: woaibocai
 * @create: 2023-11-08 14:02
 **/
@Service
public class ArticleServiceImpl implements ArticleService {
    @Resource
    private ArticleMapper articleMapper;
    @Resource
    private ArticleTagMapper articleTagMapper;
    @Resource
    private RedisTemplate<String,Object> redisTemplateObject;
    @Override
    public Result<IPage<ArticleStatusPageVo>> findPage(Integer current, Integer size, QueryArticleCriteria queryArticleCriteria) {
        IPage<ArticleStatusPageVo> page = new Page<>(current,size);
        IPage<ArticleStatusPageVo> iPage = articleMapper.findPage(page,queryArticleCriteria);
        return Result.build(iPage, ResultCodeEnum.SUCCESS);
    }

    @Override
    public void updateArticleStatus(UpdateArticleStatusDto updateArticleStatusDto) {
        articleMapper.updateArticleStatus(updateArticleStatusDto);
        redisTemplateObject.executePipelined((RedisCallback<Void>) connection ->{
            // 删除 fetchDate 的
            redisTemplateObject.delete(RedisKeyEnum.BLOG_FETCHDATE_ARTICLE_AND_URL);
            redisTemplateObject.delete(RedisKeyEnum.BLOG_FETCHDATE_BLOG_INFO);
            redisTemplateObject.delete(RedisKeyEnum.BLOG_FETCHDATE_TAG_HAS_ARTICLE_COUNT_MAP);
            // 删除文章在redis上
            redisTemplateObject.delete(RedisKeyEnum.BLOG_ARTICLE.articleUrl(updateArticleStatusDto.getUrl()));
            // 删除redis上的站点地图
            redisTemplateObject.delete(RedisKeyEnum.BLOG_SITEMAP);
            redisTemplateObject.delete(RedisKeyEnum.BLOG_RSS);
            return null;
        });

    }

    @Override
    public void deletedArticleById(String id,String url) {
        articleMapper.deletedArticleById(id);
        redisTemplateObject.executePipelined((RedisCallback<Void>) connection ->{
            redisTemplateObject.delete(RedisKeyEnum.BLOG_ARTICLE.articleUrl(url));
            // 删除redis上的站点地图
            redisTemplateObject.delete(RedisKeyEnum.BLOG_SITEMAP);
            redisTemplateObject.delete(RedisKeyEnum.BLOG_RSS);
            return null;
        });
    }

    @Override
    public Result writeArticle(WriteArticleDto writeArticleDto) {
        //先把article表中所有数据插入
        String id = UUID.randomUUID().toString().replace("-", "");
        writeArticleDto.setId(id);
        articleMapper.writeArticle(writeArticleDto);
        //再把article_tag表数据插入，可能要用到批量插入
        List<TagVo> tags = writeArticleDto.getTags();
        for (TagVo tagVo : tags) {
            String tagId = UUID.randomUUID().toString().replace("-", "");
            tagVo.setArticleTagId(tagId);
        }
        articleTagMapper.insertArticleTag(id,tags);
        redisTemplateObject.executePipelined((RedisCallback<Void>) connection ->{
            // 删除 fetchDate 的
            redisTemplateObject.delete(RedisKeyEnum.BLOG_FETCHDATE_ARTICLE_AND_URL);
            redisTemplateObject.delete(RedisKeyEnum.BLOG_FETCHDATE_BLOG_INFO);
            redisTemplateObject.delete(RedisKeyEnum.BLOG_AERICLE_INDEX);
            redisTemplateObject.delete(RedisKeyEnum.BLOG_FETCHDATE_TAG_HAS_ARTICLE_COUNT_MAP);
            // 删除redis上的站点地图
            redisTemplateObject.delete(RedisKeyEnum.BLOG_SITEMAP);
            redisTemplateObject.delete(RedisKeyEnum.BLOG_RSS);
            return null;
        });
        return Result.build(null,ResultCodeEnum.SUCCESS);
    }

    @Override
    public Result updateArticleData(String id) {
        //先查article表
        Article article = articleMapper.mySelectById(id);
        //拷贝
        WriteArticleDto writeArticleDto = BeanCopyUtils.copyBean(article, WriteArticleDto.class);
        //查articleTag表再赋值
        List<TagVo> tagVoList = articleTagMapper.selectByArticleId(writeArticleDto.getId());
        writeArticleDto.setTags(tagVoList);
        // 删除redis上的站点地图
        redisTemplateObject.executePipelined((RedisCallback<Void>) connection ->{
            redisTemplateObject.delete(RedisKeyEnum.BLOG_SITEMAP);
            redisTemplateObject.delete(RedisKeyEnum.BLOG_RSS);
            return null;
        });
        return Result.build(writeArticleDto,ResultCodeEnum.SUCCESS);
    }

    @Override
    public Result updateArticle(WriteArticleDto writeArticleDto) {
        //更新article数据
        articleMapper.updateArticle(writeArticleDto);
        //删除article tag表中的旧数据
        articleTagMapper.deleteByArticleId(writeArticleDto.getId());
        //添加article tag表中的新数据
        List<TagVo> tags = writeArticleDto.getTags();
        for (TagVo tagVo : tags) {
            String tagId = UUID.randomUUID().toString().replace("-", "");
            tagVo.setArticleTagId(tagId);
        }
        articleTagMapper.insertArticleTag(writeArticleDto.getId(),tags);
        redisTemplateObject.executePipelined((RedisCallback<Void>) connection ->{
            // 删除 fetchDate 的
            redisTemplateObject.delete(RedisKeyEnum.BLOG_FETCHDATE_ARTICLE_AND_URL);
            redisTemplateObject.delete(RedisKeyEnum.BLOG_FETCHDATE_BLOG_INFO);
            redisTemplateObject.delete(RedisKeyEnum.BLOG_FETCHDATE_TAG_HAS_ARTICLE_COUNT_MAP);
            // 删除redis上的站点地图
            redisTemplateObject.delete(RedisKeyEnum.BLOG_SITEMAP);
            redisTemplateObject.delete(RedisKeyEnum.BLOG_RSS);
            // 删除文章在redis上
            redisTemplateObject.delete(RedisKeyEnum.BLOG_ARTICLE.articleUrl(writeArticleDto.getUrl()));
            return null;
        });
        return Result.build(null,ResultCodeEnum.SUCCESS);
    }
}
