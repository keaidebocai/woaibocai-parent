package top.woaibocai.manager.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import top.woaibocai.manager.mapper.LinkMapper;
import top.woaibocai.manager.service.LinkService;
import top.woaibocai.model.common.Result;
import top.woaibocai.model.common.ResultCodeEnum;
import top.woaibocai.model.dto.manager.link.InsertLinkDto;
import top.woaibocai.model.dto.manager.link.LinkPutStatusDto;
import top.woaibocai.model.dto.manager.link.QueryLinkDto;
import top.woaibocai.model.entity.blog.Link;

import java.util.UUID;

@Service
public class LinkServiceImpl implements LinkService {
    @Resource
    private LinkMapper linkMapper;
    @Override
    public Result findAllList(Integer current, Integer size, QueryLinkDto queryLinkDto) {
        IPage<Link> iPage = new Page<>(current,size);
        IPage<Link> linkList = linkMapper.findAllList(iPage,queryLinkDto);
        return Result.build(linkList, ResultCodeEnum.SUCCESS);
    }

    @Override
    public Result putStatus(LinkPutStatusDto linkPutStatusDto) {
        if (linkPutStatusDto.getStatus().equals("1")){
            linkPutStatusDto.setStatus("0");
        }else {
            linkPutStatusDto.setStatus("1");
        }
        linkMapper.putStatus(linkPutStatusDto);
        return Result.build(null,200,"更新成功!");
    }

    @Override
    public Result deleteById(String id) {
        linkMapper.deleteById(id);
        return Result.build(null,200,"删除成功!");
    }

    @Override
    public Result insertLink(InsertLinkDto insertLinkDto) {
        String id = UUID.randomUUID().toString().replace("-", "");
        insertLinkDto.setId(id);
        linkMapper.insertLink(insertLinkDto);
        return Result.build(null,200,"添加成功!");
    }
}
