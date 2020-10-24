package com.lsu.server.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lsu.server.domain.Role;
import com.lsu.server.domain.RoleExample;
import com.lsu.server.domain.RoleResource;
import com.lsu.server.domain.RoleResourceExample;
import com.lsu.server.dto.RoleDto;
import com.lsu.server.dto.PageDto;
import com.lsu.server.mapper.RoleMapper;
import com.lsu.server.mapper.RoleResourceMapper;
import com.lsu.server.util.CopyUtil;
import com.lsu.server.util.UuidUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
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
public class RoleService {

    @Resource
    private RoleMapper roleMapper;

    @Resource
    private RoleResourceMapper roleResourceMapper;

    /**
     * 分页查询
     *
     * @return 数据
     */
    public void getAll(PageDto<RoleDto> pageDto) {
        /*
         * 分页插件的使用:
         *   规则是调用 startPage 之后遇到的第一个 select 语句会进行分页;
         * */
        PageHelper.startPage(pageDto.getPage(), pageDto.getSize());
        RoleExample roleExample = new RoleExample();
        List<Role> roleList = roleMapper.selectByExample(roleExample);
        PageInfo<Role> pageInfo = new PageInfo<>(roleList);
        pageDto.setTotal(pageInfo.getTotal());
        List<RoleDto> roleDtoList = CopyUtil.copyList(roleList, RoleDto.class);
        pageDto.setList(roleDtoList);
    }

    /**
     * 根据 ID 是否为空判断是删除还是新增
     *
     * @param roleDto 数据传输对象
     */
    public void save(RoleDto roleDto) {
        Role role = CopyUtil.copy(roleDto, Role.class);
        if (StringUtils.isEmpty(role.getId())) {
            this.insert(role);
        } else {
            this.update(role);
        }
    }

    private void insert(Role role) {
        role.setId(UuidUtil.getShortUuid());
        roleMapper.insert(role);
    }

    private void update(Role role) {
        roleMapper.updateByPrimaryKey(role);
    }

    public void delete(String id) {
        roleMapper.deleteByPrimaryKey(id);
    }

    /**
     * 按角色保存资源
     *
     * @param roleDto 角色
     */
    public void saveResource(RoleDto roleDto) {
        String roleId = roleDto.getId();
        List<String> resourceIds = roleDto.getResourceIds();
        RoleResourceExample example = new RoleResourceExample();
        example.createCriteria().andRoleIdEqualTo(roleId);
        roleResourceMapper.deleteByExample(example);

        // 保存角色资源
        for (String resourceId : resourceIds) {
            RoleResource roleResource = new RoleResource();
            roleResource.setId(UuidUtil.getShortUuid());
            roleResource.setRoleId(roleId);
            roleResource.setResourceId(resourceId);
            roleResourceMapper.insert(roleResource);
        }
    }

    /**
     * 按角色加载资源
     *
     * @param roleId 角色 ID
     * @return 返回列表
     */
    public List<String> listResource(String roleId) {
        RoleResourceExample example = new RoleResourceExample();
        example.createCriteria().andRoleIdEqualTo(roleId);
        List<RoleResource> roleResources = roleResourceMapper.selectByExample(example);
        List<String> resourceIdList = new ArrayList<>();
        for (RoleResource roleResource : roleResources) {
            resourceIdList.add(roleResource.getResourceId());
        }
        return resourceIdList;
    }
}
