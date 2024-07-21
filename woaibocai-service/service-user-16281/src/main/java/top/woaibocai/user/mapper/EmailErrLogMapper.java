package top.woaibocai.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import top.woaibocai.model.entity.email.ErrLog;

/**
* @author likebocai
* @description 针对表【email_err_log】的数据库操作Mapper
* @createDate 2024-07-18 13:11:24
* @Entity top.woaibocai.model.entity.email.ErrLog;
*/
@Mapper
public interface EmailErrLogMapper extends BaseMapper<ErrLog> {

    void listenEmailComputeErrLog(ErrLog errLog);
}




