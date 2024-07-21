package top.woaibocai.blog.service.impl;

import jakarta.annotation.Resource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import top.woaibocai.blog.mapper.CategoryMapper;
import top.woaibocai.blog.service.HeaderService;
import top.woaibocai.model.Enum.RedisKeyEnum;
import top.woaibocai.model.common.Result;
import top.woaibocai.model.Enum.ResultCodeEnum;
import top.woaibocai.model.vo.blog.category.CategoryUrlNameIconVo;

import java.util.List;

@Service
public class HeaderServiceImpl implements HeaderService {
    @Resource
    private RedisTemplate<String,CategoryUrlNameIconVo> redisTemplateObject;
    @Resource
    private CategoryMapper categoryMapper;
    @Override
    public Result<List<CategoryUrlNameIconVo>> getAllCategoryNameAndUrl() {
        // redis
        List<CategoryUrlNameIconVo> categoryInfoList =
                redisTemplateObject.opsForList().range(RedisKeyEnum.BLOG_CATEGORY_INFO, 0, -1);
        if (!categoryInfoList.isEmpty()){
            return Result.build(categoryInfoList, ResultCodeEnum.SUCCESS);
        }

        List<CategoryUrlNameIconVo> categoryUrlNameIconVoList =  categoryMapper.categoryOfUrlNameIcon();
        redisTemplateObject.opsForList().rightPushAll(RedisKeyEnum.BLOG_CATEGORY_INFO,categoryUrlNameIconVoList);
        return Result.build(categoryUrlNameIconVoList,ResultCodeEnum.SUCCESS);
    }
}
