package top.woaibocai.user.service;

import top.woaibocai.model.common.Result;
import top.woaibocai.model.dto.manager.UserLoginDto;
import top.woaibocai.model.dto.manager.UserRegisterDto;
import top.woaibocai.model.dto.user.AuthorizationsDto;
import top.woaibocai.model.vo.LoginVo;

public interface UserService {
    Result<String> login(UserLoginDto userLoginDto);

    Result register(UserRegisterDto userRegisterDto);

    Result<String> authorizations(AuthorizationsDto authorizationsDto);

    Result getUserInfo(String newToken);

    Result logout(LoginVo loginVo);
}
