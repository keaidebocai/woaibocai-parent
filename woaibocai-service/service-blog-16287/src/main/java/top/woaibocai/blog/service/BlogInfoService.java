package top.woaibocai.blog.service;

import top.woaibocai.model.common.Result;
import top.woaibocai.model.vo.blog.BlogInfoVo;

public interface BlogInfoService {
    Result<BlogInfoVo> blogInfo();
}
