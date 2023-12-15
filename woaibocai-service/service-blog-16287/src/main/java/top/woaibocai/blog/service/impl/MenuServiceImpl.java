package top.woaibocai.blog.service.impl;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import top.woaibocai.blog.mapper.MenuMapper;
import top.woaibocai.blog.service.MenuService;
import top.woaibocai.model.common.Result;
import top.woaibocai.model.common.ResultCodeEnum;
import top.woaibocai.model.entity.blog.Menu;

import java.util.List;

/**
 * @program: woaibocai-parent
 * @description: 菜单接口的实现类
 * @author: woaibocai
 * @create: 2023-12-15 12:58
 **/
@Service
public class MenuServiceImpl implements MenuService {
    @Resource
    private RedisTemplate<String,String> redisTemplate;
    @Resource
    private MenuMapper menuMapper;
    @Override
    public Result getAllMenu() {
        //先查 redis
        String menuListJson = redisTemplate.opsForValue().get("blog:menu:getAllMenu");
        if (!StringUtils.isEmpty(menuListJson)) {
            List<Menu> menuListFromRedis = JSONObject.parseArray(menuListJson, Menu.class);
            return Result.build(menuListFromRedis,ResultCodeEnum.SUCCESS);
        }
        // 查数据库
        List<Menu> menuList =  menuMapper.getAllMenu();
        //放进 redis
        redisTemplate.opsForValue().set("blog:menu:getAllMenu", JSON.toJSONString(menuList));
        return Result.build(menuList, ResultCodeEnum.SUCCESS);
    }
}
