package top.woaibocai.blog.service;

import top.woaibocai.model.common.Result;
import top.woaibocai.model.vo.blog.BlogInfoVo;
import top.woaibocai.model.vo.blog.tag.TagCloudVo;

import java.util.List;

public interface BlogInfoService {
    Result<BlogInfoVo> blogInfo();

    Result<List<TagCloudVo>> tagCloud();
}
