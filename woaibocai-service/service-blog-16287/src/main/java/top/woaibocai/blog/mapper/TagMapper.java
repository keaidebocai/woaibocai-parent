package top.woaibocai.blog.mapper;

import org.apache.ibatis.annotations.Mapper;
import top.woaibocai.model.Do.KeyValue;
import top.woaibocai.model.entity.blog.Tag;
import top.woaibocai.model.vo.blog.other.Sitemap;
import top.woaibocai.model.vo.blog.tag.TagCloudVo;

import java.util.List;

@Mapper
public interface TagMapper {
    List<Tag> tagName();

    Tag tagNameAndRemark(String tagUrl);

    List<TagCloudVo> TagNameURlColor();

    List<Sitemap> selectTagUrlAndTime();

    List<KeyValue<String, String>> selectTagIdAndTagName();
}
