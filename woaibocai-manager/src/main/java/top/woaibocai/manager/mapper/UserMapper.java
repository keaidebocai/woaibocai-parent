package top.woaibocai.manager.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import top.woaibocai.model.common.User;

@Mapper
public interface UserMapper extends BaseMapper<User> {
    //根据userName查询数据库
    User selectByUserName(String userName);
}
