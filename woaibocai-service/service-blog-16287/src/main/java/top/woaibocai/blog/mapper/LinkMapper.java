package top.woaibocai.blog.mapper;

import org.apache.ibatis.annotations.Mapper;
import top.woaibocai.model.vo.blog.link.LinkVo;

import java.util.List;

@Mapper
public interface LinkMapper {
    List<LinkVo> getAllLink();
}
