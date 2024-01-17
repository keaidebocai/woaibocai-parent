package top.woaibocai.blog.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import top.woaibocai.blog.mapper.MyCommentMapper;
import top.woaibocai.blog.service.CommentService;
import top.woaibocai.blog.service.FetchDateUtilService;
import top.woaibocai.model.Do.KeyAndValue;
import top.woaibocai.model.common.RedisKeyEnum;
import top.woaibocai.model.common.Result;
import top.woaibocai.model.common.ResultCodeEnum;
import top.woaibocai.model.dto.blog.comment.OneCommentDto;
import top.woaibocai.model.vo.blog.article.BlogArticleVo;
import top.woaibocai.model.vo.blog.comment.CommentDataVo;
import top.woaibocai.model.vo.blog.comment.OneCommentVo;
import top.woaibocai.model.dto.blog.comment.ReplyOneCommentDto;

import java.time.LocalDateTime;
import java.util.*;

/**
 * @program: woaibocai-parent
 * @description: 评论接口的实现类
 * @author: woaibocai
 * @create: 2024-01-08 13:14
 **/
@Service
public class CommentServiceimpl implements CommentService {
    @Resource
    private HashOperations<String,String, Object> hashOperationSSO;
    @Resource
    private ListOperations<String,String> listOperationSS;
    @Resource
    private RedisTemplate<String,Object> objectRedisTemplate;
    @Resource
    private MyCommentMapper myCommentMapper;
    @Resource
    private ObjectMapper objectMapper;
    @Resource
    private FetchDateUtilService fetchDateUtilService;
    @Override
    public Result getCommentByArticleId(String articleId,Long current,Long size) {
        // 查看该文章是否真实存在
        Map<String, Object> articleAndUrl = hashOperationSSO.entries(RedisKeyEnum.BLOG_FETCHDATE_ARTICLE_AND_URL);
        // 如果是null 那就要初始化redis
        Boolean hasKey = null;
        if (articleAndUrl.isEmpty()) {
            Map<String, String> articleIdAndUrlMap = fetchDateUtilService.getArticleIdAndUrlMap();
            hasKey = articleIdAndUrlMap.containsKey(articleId);
        } else {
            hasKey = articleAndUrl.containsKey(articleId);
        }

        if (!hasKey) {
            return Result.build("虚空获取",ResultCodeEnum.DATA_ERROR);
        }
        // 获取该文章的所有评论总数  并初始化
        Long articleSubTotal = fetchDateUtilService.getThisArticleCommentTotal(articleId);
        if (articleSubTotal == 0L) {
            Map<String,Object> map = new HashMap<>();
            map.put("total",0L);
            List<OneCommentVo> oneCommentVoList = new ArrayList<>();
            OneCommentVo oneCommentVo = new OneCommentVo();
            List<CommentDataVo> commentDataVoList = new ArrayList<>();
            CommentDataVo commentDataVo = new CommentDataVo();
            commentDataVoList.add(commentDataVo);
            oneCommentVo.setOneCommentVoList(commentDataVoList);
            map.put("comment",oneCommentVoList);
            return Result.build(map,ResultCodeEnum.SUCCESS);
        }

        // redis 获取该文章的一级评论
        // 不用判空的原因是 如果该文章由评论，那必然由一级评论
        List<String> oneCommentIds = new ArrayList<>();
        Long onCommentSize = listOperationSS.size(RedisKeyEnum.BLOG_COMMENT_ARTICLE.articleUrl(articleId));
        if (onCommentSize == 0L) {
            List<String> list = fetchDateUtilService.initOneCommentList(articleId);
            int size1 = list.size();
            int start = (int) (size * (current -1));
            int end = (int) ((size * current) - 1);
            if (end > size1 - 1) {
                end = size1;
            }
            oneCommentIds.addAll(list.subList(start,end));
        } else {
            long start = size * (current -1);
            long end = (size * current) - 1;
            if (end > onCommentSize - 1) {
                end = onCommentSize - 1;
            }
            oneCommentIds.addAll(listOperationSS.range(RedisKeyEnum.BLOG_COMMENT_ARTICLE.articleUrl(articleId), start, end));
        }

        // 批量获取 一级评论 和 一级评论维护的二级评论的列表id
        List<OneCommentVo> oneCommentVoList = new ArrayList<>();
        Map<String,List<String>> allTwoComments = new HashMap<>();
        // 注意 Pipelined 管道只缓存一次性发送，一次性获取 不可在逻辑里把渠道的结果在放进命令中操作
        objectRedisTemplate.executePipelined((RedisCallback<Void>) connection -> {
            // 获取 所有一级评论的数据
            oneCommentIds.forEach(oneCommentId -> {
                Map<String, Object> entries = hashOperationSSO.entries(RedisKeyEnum.BLOG_COMMENT_ALL.comment(oneCommentId));
                // 如果 是空的就初始化这条一级评论
                if (entries.isEmpty()) {
                    OneCommentVo oneCommentVo = fetchDateUtilService.initOneComment(oneCommentId);
                    oneCommentVoList.add(oneCommentVo);
                } else {
                    OneCommentVo oneCommentVo = objectMapper.convertValue(entries, OneCommentVo.class);
                    oneCommentVoList.add(oneCommentVo);
                }
                List<String> twoCommentIds
                        = listOperationSS.range(RedisKeyEnum.BLOG_COMMENT_ONECOMMENT.comment(oneCommentId), 0, -1);
                if (twoCommentIds.isEmpty()) {
                    List<String> list = fetchDateUtilService.initTwoCommentListByOneId(oneCommentId);
                    if (!list.isEmpty()) {
                        twoCommentIds.addAll(list);
                        allTwoComments.put(oneCommentId,twoCommentIds);
                    }
                }
                allTwoComments.put(oneCommentId,twoCommentIds);

            });
            return null;
        });
        Map<String,List<CommentDataVo>> oneAndTwoComment = new HashMap<>();
        // 获取所有 二级评论
        objectRedisTemplate.executePipelined((RedisCallback<Void>) connection ->{
            allTwoComments.forEach((key,value) -> {
                if (!value.isEmpty()) {
                    List<CommentDataVo> list = new ArrayList<>();
                    for (String id : value) {
                        Map<String, Object> entries = hashOperationSSO.entries(RedisKeyEnum.BLOG_COMMENT_ALL.comment(id));
                        if (entries.isEmpty()) {
                            CommentDataVo commentDataVo = fetchDateUtilService.initTwoCommentById(id);
                            list.add(commentDataVo);
                        } else {
                            CommentDataVo commentDataVo = objectMapper.convertValue(entries, CommentDataVo.class);
                            list.add(commentDataVo);
                        }
                    }
                    oneAndTwoComment.put(key,list);
                } else {
                    oneAndTwoComment.put(key,null);
                }

            });
            return null;
        });

        // 封装 评论
        for (OneCommentVo oneCommentVo : oneCommentVoList) {
            oneCommentVo.setOneCommentVoList(oneAndTwoComment.get(oneCommentVo.getId()));
        }
        Map<String,Object> map = new HashMap<>();
        map.put("comment",oneCommentVoList);
        map.put("total",articleSubTotal);
        return Result.build(map,ResultCodeEnum.SUCCESS);
    }

    @Override
    public Result sendOneComment(OneCommentDto oneCommentDto) {
        // 判断 BLOG_FETCHDATE_ARTICLE_AND_URL 是否在 redis 中
        Map articleIds = hashOperationSSO.entries(RedisKeyEnum.BLOG_FETCHDATE_ARTICLE_AND_URL);
        if (articleIds.isEmpty()) {
            articleIds = fetchDateUtilService.getArticleIdAndUrlMap();
        }
        boolean hasArticle =  articleIds.containsKey(oneCommentDto.getArticleId());
        if (!hasArticle) {
            return Result.build("你评论了一个没有的文章，如评！",ResultCodeEnum.DATA_ERROR);
        }
        // 1. 存 数据库
        // 获取该用户的头像和用户名
        // 用 gateway 做验证了所以这里不可能为空
        KeyAndValue avaterAndUserName = myCommentMapper.selectUserNameAndAvaterByUserId(oneCommentDto.getUserId());
        String id = UUID.randomUUID().toString().replace("-", "");
        // 插入数据库
        myCommentMapper.saveComment(oneCommentDto,id);
        // 存入redis做准备
        OneCommentVo oneCommentVo = new OneCommentVo();
        oneCommentVo.setId(id);
        oneCommentVo.setSendId(oneCommentDto.getUserId());
        oneCommentVo.setContent(oneCommentDto.getContent());
        oneCommentVo.setAddress(oneCommentDto.getAddress());
        oneCommentVo.setCreateTime(LocalDateTime.now());
        oneCommentVo.setSendUserAvater(avaterAndUserName.getMyKey());
        oneCommentVo.setSandUserNickName(avaterAndUserName.getMyValue());

        // 2. 存 redis
        Map<String,String> map = objectMapper.convertValue(oneCommentVo,Map.class);
        hashOperationSSO.putAll(RedisKeyEnum.BLOG_COMMENT_ALL.comment(id),map);
        // 查询一级评论列表在redis中是否存在 方便添加列表
        Long hasSize = listOperationSS.size(RedisKeyEnum.BLOG_COMMENT_ARTICLE.articleUrl(oneCommentDto.getArticleId()));
        // redis 中不存在就 初始化redis  存在就直接添加id
        if (hasSize == 0L) {
            fetchDateUtilService.initOneCommentList(oneCommentDto.getArticleId());
            hashOperationSSO.increment(RedisKeyEnum.BLOG_COMMENT_COUNT,oneCommentDto.getArticleId(),1L);
            return Result.build(oneCommentVo,ResultCodeEnum.SUCCESS);
        }
        // 如果不是0 那就有可以直接加
        listOperationSS.rightPush(RedisKeyEnum.BLOG_COMMENT_ARTICLE.articleUrl(oneCommentDto.getArticleId()),id);
        // 总评论数 +1
        hashOperationSSO.increment(RedisKeyEnum.BLOG_COMMENT_COUNT,oneCommentDto.getArticleId(),1L);
        return Result.build(oneCommentVo,ResultCodeEnum.SUCCESS);
    }

    @Override
    public Result replyOneComment(ReplyOneCommentDto replyOneCommentVo) {
        // 查看 被回复的评论是否存在
        Long size = hashOperationSSO.size(RedisKeyEnum.BLOG_COMMENT_ALL.comment(replyOneCommentVo.getReplyCommentId()));
        // 存在就直接下一步 插入数据库
        if (size == 0L ) {
            // 查数据库 是否真的没有 如果有就初始化redis 没有就 报错
            OneCommentVo oneCommentVo = myCommentMapper.selectOneCommentById(replyOneCommentVo.getReplyCommentId());
            if (oneCommentVo == null) {
                return Result.build("你无法回复一条不存在的评论",ResultCodeEnum.DATA_ERROR);
            }
            KeyAndValue keyAndValue = myCommentMapper.selectUserNameAndAvaterByUserId(replyOneCommentVo.getReplyCommentUserId());
            oneCommentVo.setSendUserAvater(keyAndValue.getMyKey());
            oneCommentVo.setSandUserNickName(keyAndValue.getMyValue());
            Map map = objectMapper.convertValue(oneCommentVo, Map.class);
            // 初始化 redis
            hashOperationSSO.putAll(RedisKeyEnum.BLOG_COMMENT_ALL.comment(replyOneCommentVo.getReplyCommentId()),map);
        }

        // size ！= 0L 说明他真实存在 直接插入数据库就可以了
        String id = UUID.randomUUID().toString().replace("-", "");
        myCommentMapper.saveSubComment(replyOneCommentVo,id);
        CommentDataVo commentDataVo = new CommentDataVo();
        KeyAndValue replyAvaterAndName = myCommentMapper.selectUserNameAndAvaterByUserId(replyOneCommentVo.getReplyCommentUserId());
        KeyAndValue sendAvaterAndName = myCommentMapper.selectUserNameAndAvaterByUserId(replyOneCommentVo.getSendCommentUserId());
        commentDataVo.setId(id);
        commentDataVo.setSendId(replyOneCommentVo.getSendCommentUserId());
        commentDataVo.setSendUserAvater(sendAvaterAndName.getMyKey());
        commentDataVo.setSendUserNickName(sendAvaterAndName.getMyValue());
        commentDataVo.setReplyId(replyOneCommentVo.getReplyCommentUserId());
        commentDataVo.setReplyUserAvater(replyAvaterAndName.getMyKey());
        commentDataVo.setReplyUserNickName(replyAvaterAndName.getMyValue());
        commentDataVo.setCreateTime(LocalDateTime.now());
        commentDataVo.setContent(replyOneCommentVo.getContent());
        commentDataVo.setParentId(replyOneCommentVo.getParentId());
        commentDataVo.setAddress(replyOneCommentVo.getAddress());
        commentDataVo.setReplyCommentId(replyOneCommentVo.getReplyCommentId());
        // 插入数据库完事了，把数据推上 redis 然后 给list集合 在右边 push 上去
        Map map = objectMapper.convertValue(commentDataVo, Map.class);
        hashOperationSSO.putAll(RedisKeyEnum.BLOG_COMMENT_ALL.comment(id),map);
        listOperationSS.rightPush(RedisKeyEnum.BLOG_COMMENT_ONECOMMENT.comment(replyOneCommentVo.getParentId()),id);
        // 更新 总评论条数
        hashOperationSSO.increment(RedisKeyEnum.BLOG_COMMENT_COUNT,replyOneCommentVo.getArticleId(),1L);
        // 给前端返回 数据 用来 push 数组
        return Result.build(commentDataVo,ResultCodeEnum.SUCCESS);
    }
}
