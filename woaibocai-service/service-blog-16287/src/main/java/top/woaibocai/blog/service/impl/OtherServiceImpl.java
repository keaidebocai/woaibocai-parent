package top.woaibocai.blog.service.impl;

import com.alibaba.druid.util.StringUtils;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import top.woaibocai.blog.mapper.ArticleMapper;
import top.woaibocai.blog.service.OtherService;
import top.woaibocai.model.common.RedisKeyEnum;
import top.woaibocai.model.common.Result;
import top.woaibocai.model.common.ResultCodeEnum;

/**
 * @program: woaibocai-parent
 * @description: 杂七杂八
 * @author: woaibocai
 * @create: 2024-01-24 19:13
 **/
@Service
public class OtherServiceImpl implements OtherService {
    @Resource
    private ArticleMapper articleMapper;
    @Resource
    private RedisTemplate<String,String> redisTemplateString;
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
}
