package top.woaibocai.blog.service;

import top.woaibocai.model.common.Result;
import top.woaibocai.model.vo.blog.other.RSSVo;
import top.woaibocai.model.vo.blog.other.Sitemap;

import java.util.List;

public interface OtherService {
    Result<String> about();

    Result<String> link();

    List<Sitemap> sitemap();

    List<RSSVo> RSS();

}
