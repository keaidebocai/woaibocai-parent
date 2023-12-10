package top.woaibocai.user.service;

import top.woaibocai.model.common.Result;
import top.woaibocai.model.dto.manager.UserLoginDto;
import top.woaibocai.model.dto.manager.UserRegisterDto;

public interface UserService {
    Result<String> login(UserLoginDto userLoginDto);

    Result register(UserRegisterDto userRegisterDto);
}
