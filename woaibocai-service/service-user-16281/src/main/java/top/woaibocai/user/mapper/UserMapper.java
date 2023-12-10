package top.woaibocai.user.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import top.woaibocai.model.DO.user.UserLoginDo;
import top.woaibocai.model.dto.manager.UserRegisterDto;

@Mapper
public interface UserMapper {
    UserLoginDo selectByUserName(String userName);

    void updateRefreshTokenById(String id, String refreshToken);

    Integer userNameValidate(String userName);

    Integer emailValidate(String email);

    Boolean register(@Param("userRegisterDto") UserRegisterDto userRegisterDto, @Param("id") String id);


//    void register(@Param("userRegisterDto") UserRegisterDto userRegisterDto, @Param("id") String id);
}
