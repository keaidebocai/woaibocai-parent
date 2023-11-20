package top.woaibocai.manager.service;


import top.woaibocai.model.common.Result;
import top.woaibocai.model.dto.QueryTagDto;


public interface TagService {
    Result findByTageName(Integer current, Integer size, QueryTagDto queryTagDto);

    Result insertTag(QueryTagDto queryTagDto);

    Result deleted(String id);
}
