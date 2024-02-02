package top.woaibocai.user.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import top.woaibocai.model.Do.user.UserLoginDo;
import top.woaibocai.model.common.User;
import top.woaibocai.model.dto.manager.UserRegisterDto;
import top.woaibocai.model.vo.user.UserInfoVo;

@Mapper
public interface UserMapper {
    UserLoginDo selectByUserName(String userName);

    void updateRefreshTokenById(String id, String refreshToken);

    Integer userNameValidate(String userName);

    Integer emailValidate(String email);

    Boolean register(@Param("userRegisterDto") UserRegisterDto userRegisterDto, @Param("id") String id);

    UserInfoVo userInfo(String id);

    Integer nickNameValidate(String nickName);

    void updatePasswordById(String id, String password);

    User selectIdEmailByUserName(String userName);

    String selectUserNameByEmail(String email);

    void updateUserInfo(UserInfoVo userInfoVo);
}
