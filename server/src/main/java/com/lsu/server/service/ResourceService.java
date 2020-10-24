package com.lsu.server.service;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lsu.server.domain.Resource;
import com.lsu.server.domain.ResourceExample;
import com.lsu.server.dto.PageDto;
import com.lsu.server.dto.ResourceDto;
import com.lsu.server.mapper.ResourceMapper;
import com.lsu.server.util.CopyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 业务层
 *
 * @Author wang suo
 * @Date 2020/10/9 0009 20:49
 * @Version 1.0
 */
@Service
public class ResourceService {

    private static final Logger LOG = LoggerFactory.getLogger(ResourceService.class);

    @javax.annotation.Resource
    private ResourceMapper resourceMapper;

    /**
     * 分页查询
     *
     * @return 数据
     */
    public void getAll(PageDto<ResourceDto> pageDto) {
        /*
         * 分页插件的使用:
         *   规则是调用 startPage 之后遇到的第一个 select 语句会进行分页;
         * */
        PageHelper.startPage(pageDto.getPage(), pageDto.getSize());
        ResourceExample resourceExample = new ResourceExample();
        List<Resource> resourceList = resourceMapper.selectByExample(resourceExample);
        PageInfo<Resource> pageInfo = new PageInfo<>(resourceList);
        pageDto.setTotal(pageInfo.getTotal());
        List<ResourceDto> resourceDtoList = CopyUtil.copyList(resourceList, ResourceDto.class);
        pageDto.setList(resourceDtoList);
    }

    /**
     * 根据 ID 是否为空判断是删除还是新增
     *
     * @param resourceDto 数据传输对象
     */
    public void save(ResourceDto resourceDto) {
        Resource resource = CopyUtil.copy(resourceDto, Resource.class);
        if (StringUtils.isEmpty(resource.getId())) {
            this.insert(resource);
        } else {
            this.update(resource);
        }
    }

    private void insert(Resource resource) {
        resourceMapper.insert(resource);
    }

    private void update(Resource resource) {
        resourceMapper.updateByPrimaryKey(resource);
    }

    public void delete(String id) {
        resourceMapper.deleteByPrimaryKey(id);
    }

    /**
     * 保存 json 数据结构
     *
     * @param jsonStr json 字符串
     */
    @Transactional(rollbackFor = Exception.class)
    public void saveJson(String jsonStr) {
        // 有嵌套的 ResourceDto
        List<ResourceDto> jsonList = JSON.parseArray(jsonStr, ResourceDto.class);
        // 没有嵌套的 ResourceDto
        List<ResourceDto> list = new ArrayList<>();
        if (!CollectionUtils.isEmpty(jsonList)) {
            for (ResourceDto resourceDto : jsonList) {
                resourceDto.setParent("");
                add(list, resourceDto);
            }
        }
        LOG.info("共{}条", list.size());
        resourceMapper.deleteByExample(null);
        for (ResourceDto resourceDto : list) {
            this.insert(CopyUtil.copy(resourceDto, Resource.class));
        }
    }

    /**
     * 递归: 将树形结构的节点全部取出来: 放到 list 中
     * 将 ResourceDto 中的 List 拆成 ResourceDto
     *
     * @param list        没有嵌套的集合
     * @param resourceDto 有嵌套的对象
     */
    private void add(List<ResourceDto> list, ResourceDto resourceDto) {
        list.add(resourceDto);

        List<ResourceDto> childrenList = resourceDto.getChildren();
        if (!CollectionUtils.isEmpty(childrenList)) {
            for (ResourceDto dto : childrenList) {
                dto.setParent(resourceDto.getId());
                add(list, dto);
            }
        }
    }

    /**
     * 按约定将列表转成树
     * ID 要正序排列
     *
     * @return 集合
     */
    public List<ResourceDto> loadTree() {
        ResourceExample example = new ResourceExample();
        example.setOrderByClause("id asc");
        List<Resource> resources = resourceMapper.selectByExample(example);
        List<ResourceDto> resourceDtoList = CopyUtil.copyList(resources, ResourceDto.class);
        for (int i = resourceDtoList.size() - 1; i >= 0; i--) {
            // 当前要移动的记录
            ResourceDto child = resourceDtoList.get(i);
            // 如果没有父节点就不往下了
            if (StringUtils.isEmpty(child.getParent())) {
                continue;
            }
            // 查找父节点
            for (int j = i - 1; j >=0; j--) {
                ResourceDto parent = resourceDtoList.get(j);
                if (child.getParent().equals(parent.getId())) {
                    if (CollectionUtils.isEmpty(parent.getChildren())) {
                        parent.setChildren(new ArrayList<>());
                    }
                    parent.getChildren().add(0, child);
                    resourceDtoList.remove(child);
                }
            }
        }
        return resourceDtoList;
    }
}
