package top.woaibocai.blog.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.stereotype.Service;
import top.woaibocai.blog.service.BlogInfoService;
import top.woaibocai.blog.service.FetchDateUtilService;
import top.woaibocai.model.common.RedisKeyEnum;
import top.woaibocai.model.common.Result;
import top.woaibocai.model.common.ResultCodeEnum;
import top.woaibocai.model.vo.blog.BlogInfoVo;

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
}
