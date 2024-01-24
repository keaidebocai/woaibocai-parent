package top.woaibocai.blog.service.impl;

import jakarta.annotation.Resource;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import top.woaibocai.blog.mapper.LinkMapper;
import top.woaibocai.blog.service.LinkService;
import top.woaibocai.model.common.RedisKeyEnum;
import top.woaibocai.model.common.Result;
import top.woaibocai.model.common.ResultCodeEnum;
import top.woaibocai.model.vo.blog.category.CategoryUrlNameIconVo;
import top.woaibocai.model.vo.blog.link.LinkVo;

import java.util.List;

/**
 * @program: woaibocai-parent
 * @description: 友链
 * @author: woaibocai
 * @create: 2024-01-24 12:30
 **/
@Service
public class LinkServiceImpl implements LinkService {
    @Resource
    private LinkMapper linkMapper;
    @Resource
    private RedisTemplate<String, LinkVo> redisTemplateObject;
    @Override
    public Result<List<LinkVo>> getAllLink() {
        List<LinkVo> range = redisTemplateObject.opsForList().range(RedisKeyEnum.BLOG_LINK_ALL, 0, -1);
        if (range.isEmpty()) {
            List<LinkVo> linkVos = linkMapper.getAllLink();
            redisTemplateObject.opsForList().rightPushAll(RedisKeyEnum.BLOG_LINK_ALL,linkVos);
            return Result.build(linkVos,ResultCodeEnum.SUCCESS);
        }
        return Result.build(range, ResultCodeEnum.SUCCESS);
    }
}
