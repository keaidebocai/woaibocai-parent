package top.woaibocai.manager.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import top.woaibocai.model.common.Result;
import top.woaibocai.model.dto.InsertLinkDto;
import top.woaibocai.model.dto.LinkPutStatusDto;
import top.woaibocai.model.dto.QueryLinkDto;
import top.woaibocai.model.entity.blog.Link;

import java.util.List;

@Mapper
public interface LinkMapper {
    IPage<Link> findAllList(@Param("iPage") IPage<Link> iPage, @Param("queryLinkDto") QueryLinkDto queryLinkDto);

    void putStatus(LinkPutStatusDto linkPutStatusDto);

    void deleteById(String id);

    void insertLink(InsertLinkDto insertLinkDto);
}
