package top.woaibocai.manager.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import top.woaibocai.model.common.Result;
import top.woaibocai.model.dto.UpdateArticleStatusDto;
import top.woaibocai.model.vo.manager.ArticlePageVo;

import java.util.List;

public interface ArticleService {
    Result<IPage<ArticlePageVo>> findPage(Integer current, Integer size);

    void updateArticleStatus(UpdateArticleStatusDto updateArticleStatusDto);

    void deletedArticleById(Integer id);
}
