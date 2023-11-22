package top.woaibocai.manager.service;

import top.woaibocai.model.common.Result;
import top.woaibocai.model.dto.InsertLinkDto;
import top.woaibocai.model.dto.LinkPutStatusDto;
import top.woaibocai.model.dto.QueryLinkDto;

public interface LinkService {
    Result findAllList(Integer current, Integer size, QueryLinkDto queryLinkDto);

    Result putStatus(LinkPutStatusDto linkPutStatusDto);

    Result deleteById(String id);

    Result insertLink(InsertLinkDto insertLinkDto);
}
