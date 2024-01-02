package top.woaibocai.blog.service.impl;

import jakarta.annotation.Resource;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import top.woaibocai.blog.mapper.CategoryMapper;
import top.woaibocai.blog.mapper.MenuMapper;
import top.woaibocai.blog.service.HeaderService;
import top.woaibocai.model.common.RedisKeyEnum;
import top.woaibocai.model.common.Result;
import top.woaibocai.model.common.ResultCodeEnum;
import top.woaibocai.model.entity.blog.Menu;
import top.woaibocai.model.vo.blog.category.CategoryAllVo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class HeaderServiceImpl implements HeaderService {
    @Resource
    private HashOperations<String,String,String> hashOperationSSS ;
    @Resource
    RedisTemplate<String,Menu> redisTemplateObject;
    @Resource
    private CategoryMapper categoryMapper;
    @Resource
    private MenuMapper menuMapper;
    @Override
    public Result getCategoryAndMenu() {
        // getAllCategory
        // 查询 redis 有没有
        Map<String, String> entries = hashOperationSSS.entries(RedisKeyEnum.BLOG_HEADER_GET_ALL_CATEGORY);
        // 没有 就初始化
        if (entries.isEmpty()) {
            List<CategoryAllVo> allCategory = categoryMapper.getAllCategory();
            Map<String,String> cateCategoryMap = new HashMap<>();
            allCategory.forEach(category -> cateCategoryMap.put(category.getId(),category.getCategoryName()));
            hashOperationSSS.putAll(RedisKeyEnum.BLOG_HEADER_GET_ALL_CATEGORY,cateCategoryMap);
            entries = cateCategoryMap;
        }
        List<CategoryAllVo> data = new ArrayList<>();
        entries.forEach((key,value) -> data.add(new CategoryAllVo(key,value)) );

        // getAllMenu
        // 查询redis
        List<Menu> menuList = redisTemplateObject.opsForList().range(RedisKeyEnum.BLOG_HEADER_GET_ALL_MENU, 0,-1);
        if (menuList.isEmpty()) {
            List<Menu> allMenu = menuMapper.getAllMenu();
            redisTemplateObject.opsForList().leftPushAll(RedisKeyEnum.BLOG_HEADER_GET_ALL_MENU,allMenu);
            menuList = allMenu;
        }
        Map<String,Object> dataVo = new HashMap<>();
        dataVo.put("category",data);
        dataVo.put("menu",menuList);
        // 封装返回
        return Result.build(dataVo, ResultCodeEnum.SUCCESS);
    }
}
