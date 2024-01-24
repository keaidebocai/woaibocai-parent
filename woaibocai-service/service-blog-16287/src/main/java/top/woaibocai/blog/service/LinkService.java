package top.woaibocai.blog.service;

import top.woaibocai.model.common.Result;
import top.woaibocai.model.vo.blog.link.LinkVo;

import java.util.List;

public interface LinkService {
    Result<List<LinkVo>> getAllLink();

}
