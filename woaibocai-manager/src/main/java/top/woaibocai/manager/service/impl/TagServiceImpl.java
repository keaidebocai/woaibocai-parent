package top.woaibocai.manager.service.impl;

import com.alibaba.druid.util.StringUtils;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import top.woaibocai.manager.mapper.TagMapper;
import top.woaibocai.manager.service.TagService;
import top.woaibocai.model.Enum.RedisKeyEnum;
import top.woaibocai.model.common.Result;
import top.woaibocai.model.Enum.ResultCodeEnum;
import top.woaibocai.model.dto.manager.tag.QueryTagDto;
import top.woaibocai.model.entity.blog.Tag;
import top.woaibocai.model.vo.manager.TagVo;


import java.util.List;
import java.util.UUID;

@Service
public class TagServiceImpl implements TagService {
    @Resource
    private TagMapper tagMapper;
    @Resource
    private RedisTemplate<String,Object> redisTemplateObject;
    @Override
    public Result findByTageName(Integer current, Integer size, QueryTagDto queryTagDto) {
        //先放进IPage容器里
        IPage<Tag> iPage = new Page<>(current,size);
        //把条件和ipage放进mapper里进行查询
        IPage<Tag> articleTagList = tagMapper.findByTageName(iPage,queryTagDto);
        return Result.build(articleTagList, ResultCodeEnum.SUCCESS);
    }

    @Override
    public Result insertTag(QueryTagDto queryTagDto) {
        redisTemplateObject.executePipelined((RedisCallback<Void>) connection ->{
            redisTemplateObject.delete(RedisKeyEnum.BLOG_FETCHDATE_TAG_HAS_ARTICLE_COUNT_MAP);
            redisTemplateObject.delete(RedisKeyEnum.BLOG_TAG_ALL_INFO);
            redisTemplateObject.delete(RedisKeyEnum.BLOG_FETCHDATE_BLOG_INFO);
            // 删除redis上的站点地图
            redisTemplateObject.delete(RedisKeyEnum.BLOG_SITEMAP);
            redisTemplateObject.delete(RedisKeyEnum.BLOG_RSS);
            return null;
        });
        if (StringUtils.isEmpty(queryTagDto.getRemark()) || StringUtils.isEmpty(queryTagDto.getTagName())){
            return Result.build(null,ResultCodeEnum.DATA_ISNOLL);
        }
        if (!StringUtils.isEmpty(queryTagDto.getId())){
            tagMapper.updateTag(queryTagDto);
            return Result.build(null,114,"更新成功!");
        }
        String id = UUID.randomUUID().toString().replace("-", "");
        queryTagDto.setId(id);
        tagMapper.insertTag(queryTagDto);
        redisTemplateObject.delete(RedisKeyEnum.BLOG_SITEMAP);
        return Result.build(null,ResultCodeEnum.SUCCESS);
    }

    @Override
    public Result deleted(String id) {
        tagMapper.deleted(id);
        redisTemplateObject.executePipelined((RedisCallback<Void>) connection ->{
            // 删除 redis
            redisTemplateObject.delete(RedisKeyEnum.BLOG_FETCHDATE_TAG_HAS_ARTICLE_COUNT_MAP);
            redisTemplateObject.delete(RedisKeyEnum.BLOG_TAG_ALL_INFO);
            redisTemplateObject.delete(RedisKeyEnum.BLOG_FETCHDATE_BLOG_INFO);
            // 删除redis上的站点地图
            redisTemplateObject.delete(RedisKeyEnum.BLOG_SITEMAP);
            redisTemplateObject.delete(RedisKeyEnum.BLOG_RSS);
            return null;
        });
        return Result.build(null,ResultCodeEnum.SUCCESS);
    }

    @Override
    public Result getAllTagAndId() {
        List<TagVo> tagVoList = tagMapper.getAllTagAndId();
        return Result.build(tagVoList,ResultCodeEnum.SUCCESS);
    }
}
