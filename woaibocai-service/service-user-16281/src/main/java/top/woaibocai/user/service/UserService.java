package top.woaibocai.user.service;

import top.woaibocai.model.common.Result;
import top.woaibocai.model.dto.manager.UserLoginDto;
import top.woaibocai.model.dto.manager.UserRegisterDto;
import top.woaibocai.model.dto.user.AuthorizationsDto;
import top.woaibocai.model.dto.user.UserForgotDto;
import top.woaibocai.model.vo.LoginVo;
import top.woaibocai.model.vo.user.UserInfoVo;

public interface UserService {
    Result<String> login(UserLoginDto userLoginDto);

    Result register(UserRegisterDto userRegisterDto);

    Result<String> authorizations(AuthorizationsDto authorizationsDto);

    Result getUserInfo(String newToken);

    Result logout(LoginVo loginVo);

    Result registerEmail(String email);

    Result forgot(UserForgotDto userForgotDto);

    Result forgotEmail(String email,String userName);

    Result updateUserInfo(UserInfoVo userInfoVo);
}
