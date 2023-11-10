package top.woaibocai.manager.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import top.woaibocai.common.exception.BoCaiException;
import top.woaibocai.manager.mapper.ArticleMapper;
import top.woaibocai.manager.service.ArticleService;
import top.woaibocai.model.common.Result;
import top.woaibocai.model.common.ResultCodeEnum;
import top.woaibocai.model.dto.UpdateArticleStatusDto;
import top.woaibocai.model.entity.blog.Article;
import top.woaibocai.model.vo.manager.ArticlePageVo;

import java.util.List;

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
    private RedisTemplate<String,String> redisTemplate;
    @Override
    public Result<IPage<ArticlePageVo>> findPage(Integer current, Integer size,String newToken) {
        String userInfoJson = redisTemplate.opsForValue().get("user::token:" + newToken);
        if (!StringUtils.hasText(userInfoJson)){
            return Result.build(null,ResultCodeEnum.LOGIN_NOLL);
//            throw new BoCaiException(ResultCodeEnum.LOGIN_NOLL);
        }
        IPage<ArticlePageVo> page = new Page<>(current,size);
        IPage<ArticlePageVo> iPage = articleMapper.findPage(page);
        return Result.build(iPage, ResultCodeEnum.SUCCESS);
    }

    @Override
    public void updateArticleStatus(UpdateArticleStatusDto updateArticleStatusDto) {
        articleMapper.updateArticleStatus(updateArticleStatusDto);
    }

    @Override
    public void deletedArticleById(Integer id) {
        articleMapper.deletedArticleById(id);
    }
}
