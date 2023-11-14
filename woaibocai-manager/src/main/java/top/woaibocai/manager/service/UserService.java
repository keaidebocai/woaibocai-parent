package top.woaibocai.manager.service;

import top.woaibocai.model.common.Result;
import top.woaibocai.model.dto.UserLoginDto;
import top.woaibocai.model.vo.LoginVo;

public interface UserService{

    Result<String> login(UserLoginDto userLoginDTO);

    Result getUserInfo(String token);
    //用refresh_token更新失效的token
    Result<String> authorizations(String refresh_token);

    Result logout(String newToken,LoginVo loginVo);
}
