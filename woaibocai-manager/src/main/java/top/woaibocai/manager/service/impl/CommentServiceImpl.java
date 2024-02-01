package top.woaibocai.manager.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import top.woaibocai.manager.mapper.CommentMapper;
import top.woaibocai.manager.mapper.UserMapper;
import top.woaibocai.manager.service.CommentService;
import top.woaibocai.model.common.RedisKeyEnum;
import top.woaibocai.model.common.Result;
import top.woaibocai.model.common.ResultCodeEnum;
import top.woaibocai.model.common.User;
import top.woaibocai.model.dto.manager.comment.QueryCommentDto;
import top.woaibocai.model.entity.blog.Comment;
import top.woaibocai.model.vo.manager.CommentVo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CommentServiceImpl implements CommentService {
    @Resource
    private CommentMapper commentMapper;
    @Resource
    private RedisTemplate<String,Object> redisTemplateObject;
    @Override
    public Result list(Integer current, Integer size, QueryCommentDto queryCommentDto) {
        IPage<CommentVo> commentVoIPage = new Page<>(current,size);
        IPage<CommentVo> iPage = commentMapper.list(commentVoIPage,queryCommentDto);
        //返回
        return Result.build(iPage,ResultCodeEnum.SUCCESS);
    }

    @Override
    public Result deleteById(String id,String blogArticleId) {
        commentMapper.deleteById(id);
        redisTemplateObject.executePipelined((RedisCallback<Void>) connection ->{
            redisTemplateObject.delete(RedisKeyEnum.BLOG_COMMENT_ALL.comment(id));
            redisTemplateObject.delete(RedisKeyEnum.BLOG_COMMENT_ARTICLE.comment(blogArticleId));
            redisTemplateObject.delete(RedisKeyEnum.BLOG_COMMENT_COUNT);
            // 删除redis上的站点地图
            redisTemplateObject.delete(RedisKeyEnum.BLOG_SITEMAP);
            redisTemplateObject.delete(RedisKeyEnum.BLOG_RSS);
            return null;
        });
        return Result.build(null,ResultCodeEnum.SUCCESS);
    }

}
