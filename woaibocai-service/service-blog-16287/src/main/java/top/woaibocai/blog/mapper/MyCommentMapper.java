package top.woaibocai.blog.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import top.woaibocai.model.Do.KeyAndValue;
import top.woaibocai.model.dto.blog.comment.OneCommentDto;
import top.woaibocai.model.dto.blog.comment.ReplyOneCommentDto;
import top.woaibocai.model.vo.blog.comment.CommentDataVo;
import top.woaibocai.model.vo.blog.comment.OneCommentVo;

import java.util.List;

@Mapper
public interface MyCommentMapper {

    KeyAndValue selectUserNameAndAvaterByUserId(String userId);

    void saveComment(@Param("oneCommentDto") OneCommentDto oneCommentDto,@Param("id") String id);

    List<String> selectOneCommentListByArticleId(String articleId);

    OneCommentVo selectOneCommentById(String replyCommentId);

    void saveSubComment(@Param("replyOneCommentVo") ReplyOneCommentDto replyOneCommentVo, @Param("id") String id);

    List<KeyAndValue> getAllArticleCommentTotal();

    List<String> selectTwoCommentIdByOneId(String oneCommentId);

    CommentDataVo selectCommentDataVo(String id);
}
