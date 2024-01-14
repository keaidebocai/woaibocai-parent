package top.woaibocai.blog.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.stereotype.Service;
import top.woaibocai.blog.mapper.MyCommentMapper;
import top.woaibocai.blog.service.CommentService;
import top.woaibocai.blog.service.FetchDateUtilService;
import top.woaibocai.model.Do.KeyAndValue;
import top.woaibocai.model.common.RedisKeyEnum;
import top.woaibocai.model.common.Result;
import top.woaibocai.model.common.ResultCodeEnum;
import top.woaibocai.model.dto.blog.comment.OneCommentDto;
import top.woaibocai.model.vo.blog.comment.CommentDataVo;
import top.woaibocai.model.vo.blog.comment.OneCommentVo;
import top.woaibocai.model.dto.blog.comment.ReplyOneCommentDto;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

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
    private MyCommentMapper myCommentMapper;
    @Resource
    private ObjectMapper objectMapper;
    @Resource
    private FetchDateUtilService fetchDateUtilService;
    @Override
    public Result getCommentByArticleId(String articleId,Long current,Long size) {
        //
        return null;
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
        myCommentMapper.saveComment(oneCommentDto,id);
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
        // 查询以及评论列表再redis中是否存在 方便添加列表
        Long hasSize = listOperationSS.size(RedisKeyEnum.BLOG_COMMENT_ARTICLE.articleUrl(oneCommentDto.getArticleId()));
        if (hasSize == 0L) {
            fetchDateUtilService.initOneCommentList(oneCommentDto.getArticleId());
        }
        listOperationSS.rightPush(RedisKeyEnum.BLOG_COMMENT_ARTICLE.articleUrl(oneCommentDto.getArticleId()),id);
        return Result.build(oneCommentVo,ResultCodeEnum.SUCCESS);
    }

    @Override
    public Result replyOneComment(ReplyOneCommentDto replyOneCommentVo) {
        // 查看 父评论是否存在
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
        commentDataVo.setSandUserNickName(sendAvaterAndName.getMyValue());
        commentDataVo.setReplyId(replyOneCommentVo.getReplyCommentUserId());
        commentDataVo.setReplyUserAvater(replyAvaterAndName.getMyKey());
        commentDataVo.setReplyUserNickName(replyAvaterAndName.getMyValue());
        commentDataVo.setCreateTime(LocalDateTime.now());
        commentDataVo.setContent(replyOneCommentVo.getContent());
        commentDataVo.setParentId(replyOneCommentVo.getParentId());
        commentDataVo.setAddress(replyOneCommentVo.getAddress());
        // 插入数据库完事了，把数据推上 redis 然后 给list集合 在右边 push 上去
        Map map = objectMapper.convertValue(commentDataVo, Map.class);
        hashOperationSSO.putAll(RedisKeyEnum.BLOG_COMMENT_ALL.comment(id),map);
        listOperationSS.rightPush(RedisKeyEnum.BLOG_COMMENT_ONECOMMENT.comment(replyOneCommentVo.getParentId()),id);
        // 给前端返回 数据 用来 push 数组
        return Result.build(commentDataVo,ResultCodeEnum.SUCCESS);
    }
}
