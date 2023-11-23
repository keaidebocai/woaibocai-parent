package top.woaibocai.manager.service;

import top.woaibocai.model.common.Result;
import top.woaibocai.model.dto.manager.link.InsertLinkDto;
import top.woaibocai.model.dto.manager.link.LinkPutStatusDto;
import top.woaibocai.model.dto.manager.link.QueryLinkDto;

public interface LinkService {
    Result findAllList(Integer current, Integer size, QueryLinkDto queryLinkDto);

    Result putStatus(LinkPutStatusDto linkPutStatusDto);

    Result deleteById(String id);

    Result insertLink(InsertLinkDto insertLinkDto);
}
