package top.woaibocai.blog.service.impl;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import top.woaibocai.blog.mapper.ArticleMapper;
import top.woaibocai.blog.mapper.ArticleTagMapper;
import top.woaibocai.blog.mapper.TagMapper;
import top.woaibocai.blog.service.ArticleService;
import top.woaibocai.model.Do.blog.ArticleHasTagsDo;
import top.woaibocai.model.Do.blog.TagHasArticleCountDo;
import top.woaibocai.model.common.Result;
import top.woaibocai.model.common.ResultCodeEnum;
import top.woaibocai.model.entity.blog.Tag;
import top.woaibocai.model.vo.blog.article.BlogArticlePageVo;
import top.woaibocai.model.vo.blog.tag.TagInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ArticleServiceImpl implements ArticleService {
    @Resource
    private ArticleMapper articleMapper;
    @Resource
    private ArticleTagMapper articleTagMapper;
    @Resource
    private TagMapper tagMapper;
    @Override
    public Result<List<BlogArticlePageVo>> indexArticlePage(Integer current, Integer size) {
        // 联表查询所有没有删除的文章 把用户id转成用户名 userName   把文章分类转成文章分类名 blogCategoryName
        List<BlogArticlePageVo> blogArticlePageVoList = articleMapper.selectAllArticle();
        if (blogArticlePageVoList.isEmpty()) {
            return Result.build(null,ResultCodeEnum.DATA_ERROR);
        }
        // 查询每个标签中有多少文章使用 查出list  转成 map Map<tagId,Count>
        List<TagHasArticleCountDo> tagHasArticleCountDoList = articleTagMapper.tagByArticleCount();
        if (tagHasArticleCountDoList.isEmpty()) {
            return Result.build(null,ResultCodeEnum.DATA_ERROR);
        }
        Map<String,Integer> tagHasArtilceCountMap = new HashMap<>();
        for (TagHasArticleCountDo tagHasArticleCountDo : tagHasArticleCountDoList) {
            tagHasArtilceCountMap.put(tagHasArticleCountDo.getTagId(),tagHasArticleCountDo.getArticleCount());
        }
        // 查询所有标签的id和tagName 封装成map Map<String,String> tagNameMap
        List<Tag> tagNameList = tagMapper.tagName();
        if (tagNameList.isEmpty()) {
            return Result.build(null,ResultCodeEnum.DATA_ERROR);
        }
        Map<String,String> tagNameMap = new HashMap<>();
        for (Tag tag : tagNameList) {
            tagNameMap.put(tag.getId(),tag.getTagName());
        }
        // 查询每个文章的所有 标签 List<{articleId,List<String> tagIdList}> articleHasTags
        List<ArticleHasTagsDo> articleHasTags = articleTagMapper.articleHasTags();
        if (articleHasTags.isEmpty()) {
            return Result.build(null,ResultCodeEnum.DATA_ERROR);
        }
        // 把 articleHasTags 成 articleHasTagsMap Map<articleId,List<String>>
        Map<String,List<String>> articleHasTagsMap = new HashMap<>();
        for (ArticleHasTagsDo articleHasTagsDo : articleHasTags) {
            // 1b00f608b3404a44a625fc47c453a33a,afc4501939af41ae9891251bbb3bfac3,4bc6d9a666fd43869818e671b6c423f7,8aba951e77124a16a10d318c0ca07ec4,46846ca8be314f1897de862e8e5b3d32,
            String tagIdListString = articleHasTagsDo.getTagIdListString();
            int tagIdCount = (tagIdListString.length())/33;
            List<String> tagIdList = new ArrayList<>();
            for (int i =0 ;i < tagIdCount;i++) {
                // https://www.jianshu.com/p/2dfeb4968cc1 这个b不知道左开右闭！起的老子直接注册账号+绑定手机号去骂他，捏麻麻地，气死老子了！
                String tagId = tagIdListString.substring((33 * i), ((33 * i) + 32));
                tagIdList.add(tagId);
            }
            articleHasTagsMap.put(articleHasTagsDo.getArticleId(),tagIdList);
        }
        // 封装所有数据
        // 1.遍历所有文章的集合 blogArticlePageVoList
        for(BlogArticlePageVo blogArticlePageVo : blogArticlePageVoList) {
            // 文章字数 blogArticlePageVoList.content.length = articleLength
            blogArticlePageVo.setArticleLength(blogArticlePageVo.getContent().length());
            // 计算阅读时间 articleLength/300
            blogArticlePageVo.setReadingDuration(blogArticlePageVo.getContent().length()/300);
            // 1.1 给 BlogArticlePageVo 中的 tags 赋值
            // 1.1.1 给每个文章都创建 List<TagInfo> tags
            List<TagInfo> tags = new ArrayList<>();
            // 1.1.1.1 从 articleHasTagsMap 中 获取当前文章的 tag集合
            List<String> tagIdList = articleHasTagsMap.get(blogArticlePageVo.getId());
            // 1.1.1.2循环add添加 tagInfo
            for (String tagId : tagIdList) {
                // 判断 是否有值
                if (tagNameMap.get(tagId).isEmpty()) {
                    return Result.build(null, ResultCodeEnum.DATA_ERROR);
                }
                // 每个元素的tagName从 tagNameMap 获取 thisTagHasArticleCount 从  tagHasArtilceCountMap 获取
                TagInfo tagInfo = new TagInfo();
                tagInfo.setId(tagId);
                tagInfo.setTagName(tagNameMap.get(tagId));
                tagInfo.setThisTagHasArticleCount(tagHasArtilceCountMap.get(tagId));
                tags.add(tagInfo);
            }
            // 给 blogArticlePageVo 赋值
            blogArticlePageVo.setTags(tags);
        }
        // 分页
        List<BlogArticlePageVo> pageVoList = new ArrayList<>();
        int listSize = blogArticlePageVoList.size();
            // current 当前页数  size 记录行数
            for (int i =0;i < size;i++){
                int getIndex = (size * (current - 1)) + i;
                if (getIndex > listSize - 1) break;
                BlogArticlePageVo blogArticlePageVo = blogArticlePageVoList.get(getIndex);
                pageVoList.add(blogArticlePageVo);
            }
            Map<String,Object> data = new HashMap();
            data.put("data",pageVoList);
            data.put("total",listSize);
            data.put("current",current);
            data.put("size",size);
        return Result.build(data,ResultCodeEnum.SUCCESS);
    }
// 回来把所有数据删了，然后用回台添加
}
