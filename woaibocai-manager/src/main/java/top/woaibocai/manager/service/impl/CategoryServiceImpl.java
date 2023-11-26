package top.woaibocai.manager.service.impl;

import com.alibaba.druid.util.StringUtils;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import top.woaibocai.manager.mapper.CategoryMapper;
import top.woaibocai.manager.service.CategoryService;
import top.woaibocai.model.common.Result;
import top.woaibocai.model.common.ResultCodeEnum;
import top.woaibocai.model.dto.manager.category.QueryCategoryDto;
import top.woaibocai.model.dto.manager.category.UpdateCategoryDto;
import top.woaibocai.model.entity.blog.Category;
import top.woaibocai.model.others.manager.CategoryBySelector;

import java.util.List;
import java.util.UUID;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Resource
    private CategoryMapper categoryMapper;
    @Override
    public Result list(Integer current, Integer size, QueryCategoryDto queryCategoryDto) {
        IPage<Category> iPage = new Page<>(current,size);
        IPage<Category> categoryIPage = categoryMapper.queryList(iPage,queryCategoryDto);
        return Result.build(categoryIPage, ResultCodeEnum.SUCCESS);
    }

    @Override
    public Result deletedById(String id) {
        categoryMapper.deletedById(id);
        return Result.build(null,ResultCodeEnum.SUCCESS);
    }

    @Override
    public Result putOfCategory(UpdateCategoryDto updateCategoryDto) {
        if (StringUtils.isEmpty(updateCategoryDto.getId())){
            String id = UUID.randomUUID().toString().replace("-", "");
            updateCategoryDto.setId(id);
            categoryMapper.insertCategory(updateCategoryDto);
            return Result.build(null,200,"添加成功!");
        }
        categoryMapper.putOfCategory(updateCategoryDto);
        return Result.build(null,200,"更新成功!");
    }

    @Override
    public Result categorySelector() {
        //先查询所有category和对应的id
        List<CategoryBySelector> categoryBySelectors = categoryMapper.categorySelector();
        return Result.build(categoryBySelectors,ResultCodeEnum.SUCCESS);
    }
}
