package top.woaibocai.manager.service;

import top.woaibocai.model.common.Result;
import top.woaibocai.model.common.User;
import top.woaibocai.model.dto.UserLoginDTO;
import top.woaibocai.model.vo.LoginVo;

public interface UserService{

    LoginVo login(UserLoginDTO userLoginDTO);

    Result getUserInfo(String token);
    //用refresh_token更新失效的token
    LoginVo authorizations(String newToken);

}
