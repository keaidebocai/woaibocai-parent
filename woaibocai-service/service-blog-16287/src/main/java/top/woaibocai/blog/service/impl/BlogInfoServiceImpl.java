package top.woaibocai.blog.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import top.woaibocai.blog.mapper.TagMapper;
import top.woaibocai.blog.service.BlogInfoService;
import top.woaibocai.blog.service.FetchDateUtilService;
import top.woaibocai.model.Enum.RedisKeyEnum;
import top.woaibocai.model.common.Result;
import top.woaibocai.model.Enum.ResultCodeEnum;
import top.woaibocai.model.vo.blog.BlogInfoVo;
import top.woaibocai.model.vo.blog.tag.TagCloudVo;

import java.util.List;
import java.util.Map;

/**
 * @program: woaibocai-parent
 * @description: 博客基本信息实现类
 * @author: woaibocai
 * @create: 2023-12-26 17:53
 **/
@Service
public class BlogInfoServiceImpl implements BlogInfoService {
    @Resource
    private FetchDateUtilService fetchDateUtilService;
    @Resource
    private HashOperations<String,String,Object> hashOperationsSSO;
    @Resource
    private RedisTemplate<String,TagCloudVo> redisTemplateObject;
    @Resource
    private TagMapper tagMapper;
    @Override
    public Result<BlogInfoVo> blogInfo() {
        // 查询 redis
        Map<String, Object> entries = hashOperationsSSO.entries(RedisKeyEnum.BLOG_FETCHDATE_BLOG_INFO);
        ObjectMapper objectMapper = new ObjectMapper();
        if (!entries.isEmpty()){
            return Result.build(objectMapper.convertValue(entries, BlogInfoVo.class),ResultCodeEnum.SUCCESS);
        }
        return Result.build(fetchDateUtilService.getBlogInfo(),ResultCodeEnum.SUCCESS);
    }

    @Override
    public Result<List<TagCloudVo>> tagCloud() {
        List<TagCloudVo> tagCloudList = redisTemplateObject.opsForList().range(RedisKeyEnum.BLOG_TAG_ALL_INFO, 0, -1);
        if (!tagCloudList.isEmpty()) {
            return Result.build(tagCloudList,ResultCodeEnum.SUCCESS);
        }

        List<TagCloudVo> list = tagMapper.TagNameURlColor();
        redisTemplateObject.opsForList().rightPushAll(RedisKeyEnum.BLOG_TAG_ALL_INFO,list);
        return Result.build(list,ResultCodeEnum.SUCCESS);
    }
}
