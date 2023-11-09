package top.woaibocai.manager.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import top.woaibocai.model.dto.UpdateArticleStatusDto;
import top.woaibocai.model.entity.blog.Article;
import top.woaibocai.model.vo.manager.ArticlePageVo;

@Mapper
public interface ArticleMapper extends BaseMapper<Article> {
    IPage<ArticlePageVo> findPage(@Param("page") IPage<ArticlePageVo> page);

    void updateArticleStatus(UpdateArticleStatusDto updateArticleStatusDto);

    void deletedArticleById(Integer id);
}
