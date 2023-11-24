package top.woaibocai.manager.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import top.woaibocai.model.dto.manager.comment.QueryCommentDto;
import top.woaibocai.model.entity.blog.Comment;
import top.woaibocai.model.vo.manager.CommentVo;

@Mapper
public interface CommentMapper {
    IPage<CommentVo> list(@Param("commentVoIPage") IPage<CommentVo> commentVoIPage, @Param("queryCommentDto") QueryCommentDto queryCommentDto);
}
