package top.woaibocai.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import top.woaibocai.model.dto.email.ContentDto;
import top.woaibocai.model.entity.email.Content;


/**
* @author likebocai
* @description 针对表【email_content】的数据库操作Mapper
* @createDate 2024-07-18 13:11:24
* @Entity top.woaibocai.model.entity.email.Content;
*/
@Mapper
public interface EmailContentMapper extends BaseMapper<Content> {

    void emailWritingEmail(Content content);

    Content selectEmailById(String emailId);

    void updateisDeliverySucceed(String emailId);

    void updateLikeCount(String emailId);

    Content getEmailPublicVoById(String emailId);
}




