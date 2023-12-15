package top.woaibocai.blog.service.impl;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson2.JSON;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import top.woaibocai.blog.mapper.CategoryMapper;
import top.woaibocai.blog.service.CategoryService;
import top.woaibocai.model.common.Result;
import top.woaibocai.model.common.ResultCodeEnum;
import top.woaibocai.model.vo.blog.category.CategoryAllVo;

import java.util.List;

/**
 * @program: woaibocai-parent
 * @description: 分类接口的实现类
 * @author: woaibocai
 * @create: 2023-12-15 10:54
 **/
@Service
public class CategoryServiceImpl implements CategoryService {
    @Resource
    private RedisTemplate<String,String> redisTemplate;
    @Resource
    private CategoryMapper categoryMapper;
    @Override
    public Result<List<CategoryAllVo>> getAllCategory() {
        // 查询 redis 里有没有
        String categoryListJson = redisTemplate.opsForValue().get("blog:category:getAllCategory");
        if (!StringUtils.isEmpty(categoryListJson)) {
            List<CategoryAllVo> categoryAllVoList = JSONObject.parseArray(categoryListJson, CategoryAllVo.class);
            return Result.build(categoryAllVoList,ResultCodeEnum.SUCCESS);
        }
        // 查数据库
        List<CategoryAllVo> categoryAllVoList =  categoryMapper.getAllCategory();
        // 放进 redis
        //读的时候，先读缓存，缓存没有的话，就读数据库，然后取出数据后放入缓存，同时返回响应。
        //更新的时候，先更新数据库，然后再删除缓存。
        // 关于 redis 数据每次新增的分类时主动删除缓存就好了
        redisTemplate.opsForValue().set("blog:category:getAllCategory", JSON.toJSONString(categoryAllVoList));
        // 封装返回
        return Result.build(categoryAllVoList, ResultCodeEnum.SUCCESS);
    }
}
