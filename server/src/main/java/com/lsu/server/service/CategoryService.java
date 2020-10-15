package com.lsu.server.service;

import com.lsu.server.domain.Category;
import com.lsu.server.domain.CategoryExample;
import com.lsu.server.dto.CategoryDto;
import com.lsu.server.mapper.CategoryMapper;
import com.lsu.server.util.CopyUtil;
import com.lsu.server.util.UuidUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * 业务层
 *
 * @Author wang suo
 * @Date 2020/10/9 0009 20:49
 * @Version 1.0
 */
@Service
public class CategoryService {

    @Resource
    private CategoryMapper categoryMapper;

    private static final String META = "00000000";

    /**
     * 查询所有
     *
     * @return 数据
     */
    public List<CategoryDto> getAll() {
        CategoryExample categoryExample = new CategoryExample();
        categoryExample.setOrderByClause("sort asc");
        List<Category> categoryList = categoryMapper.selectByExample(categoryExample);
        return CopyUtil.copyList(categoryList, CategoryDto.class);
    }

    /**
     * 根据 ID 是否为空判断是删除还是新增
     *
     * @param categoryDto 数据传输对象
     */
    public void save(CategoryDto categoryDto) {
        Category category = CopyUtil.copy(categoryDto, Category.class);
        if (StringUtils.isEmpty(category.getId())) {
            this.insert(category);
        } else {
            this.update(category);
        }
    }

    private void insert(Category category) {
        category.setId(UuidUtil.getShortUuid());
        categoryMapper.insert(category);
    }

    private void update(Category category) {
        categoryMapper.updateByPrimaryKey(category);
    }

    /**
     * 删除分类数据
     *  添加事务
     * @param id ID
     */
    @Transactional(rollbackFor = Exception.class)
    public void delete(String id) {
        deleteChildren(id);
        categoryMapper.deleteByPrimaryKey(id);
    }

    /**
     * 删除所有的子 ID
     *
     * @param id ID 值
     */
    private void deleteChildren(String id) {
        Category category = categoryMapper.selectByPrimaryKey(id);
        if (META.equals(category.getParent())) {
            // 如果是一级分类,需要删除旗下的二级分类
            CategoryExample example = new CategoryExample();
            example.createCriteria().andParentEqualTo(category.getId());
            categoryMapper.deleteByExample(example);
        }
    }
}
