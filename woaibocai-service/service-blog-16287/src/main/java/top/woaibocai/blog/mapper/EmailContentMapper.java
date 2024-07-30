package top.woaibocai.blog.mapper;

import org.apache.ibatis.annotations.Mapper;
import top.woaibocai.model.Do.KeyValue;
import top.woaibocai.model.entity.email.Content;

import java.util.List;

/**
 * @program: woaibocai-parent
 * @description: 时光邮局-信件表
 * @author: LikeBocai
 * @create 2024/7/22 15:14
 **/
@Mapper
public interface EmailContentMapper {
    Content getEmailPublicVoById(String emailId);

    List<KeyValue<String, Long>> getEmailIdAndLikeCount();

    List<String> getNewDeliveryByDateOfYW();

    List<String> getNewWritingByDateOfY();

    KeyValue<String, String> getUserInfoByUserId(String userId);

    Long getYesDeliveryTotal();

    Long NoDeliveryTotal();
}
