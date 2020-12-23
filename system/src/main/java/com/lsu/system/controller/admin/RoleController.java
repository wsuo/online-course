package com.lsu.system.controller.admin;

import com.lsu.server.dto.RoleDto;
import com.lsu.server.dto.PageDto;
import com.lsu.server.dto.ResponseDto;
import com.lsu.server.service.RoleService;
import com.lsu.server.util.ValidatorUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 测试
 *
 * @Author wang suo
 * @Date 2020/10/9 0009 18:21
 * @Version 1.0
 */
@RestController
@RequestMapping("/admin/role")
public class RoleController {

    private static final Logger LOG = LoggerFactory.getLogger(RoleController.class);
    public static final String BUSINESS_NAME = "角色";

    @Resource
    private RoleService roleService;

    @PostMapping("/list")
    public ResponseDto<PageDto> getAll(@RequestBody PageDto<RoleDto> pageDto) {
        ResponseDto<PageDto> responseDto = new ResponseDto<>();
        roleService.getAll(pageDto);
        responseDto.setContent(pageDto);
        return responseDto;
    }

    @PostMapping("/save")
    public ResponseDto<RoleDto> save(@RequestBody RoleDto roleDto) {

        // 保存校验
        ValidatorUtil.require(roleDto.getName(), "角色");
        ValidatorUtil.length(roleDto.getName(), "角色", 1, 50);
        ValidatorUtil.require(roleDto.getDesc(), "描述");
        ValidatorUtil.length(roleDto.getDesc(), "描述", 1, 100);

        ResponseDto<RoleDto> responseDto = new ResponseDto<>();
        roleService.save(roleDto);
        responseDto.setContent(roleDto);
        return responseDto;
    }

    @DeleteMapping("/delete/{id}")
    public ResponseDto delete(@PathVariable String id) {
        ResponseDto responseDto = new ResponseDto();
        roleService.delete(id);
        return responseDto;
    }

    /**
     * 保存资源
     *
     * @param roleDto 角色
     * @return 响应
     */
    @PostMapping("/save-resource")
    public ResponseDto<RoleDto> saveResource(@RequestBody RoleDto roleDto) {
        LOG.info("保存角色资源关联开始");
        ResponseDto<RoleDto> responseDto = new ResponseDto<>();
        roleService.saveResource(roleDto);
        responseDto.setContent(roleDto);
        return responseDto;
    }

    @GetMapping("/list-resource/{roleId}")
    public ResponseDto listResource(@PathVariable String roleId) {
        LOG.info("加载资源开始");
        ResponseDto<List> responseDto = new ResponseDto<>();
        List<String> resourceIdList = roleService.listResource(roleId);
        responseDto.setContent(resourceIdList);
        return responseDto;
    }

    /**
     * 保存角色用户关联
     *
     * @param roleDto 角色dto
     * @return 响应
     */
    @PostMapping("save-user")
    public ResponseDto saveUser(@RequestBody RoleDto roleDto) {
        LOG.info("保存角色用户关联开始");
        ResponseDto<RoleDto> responseDto = new ResponseDto<>();
        roleService.saveUser(roleDto);
        responseDto.setContent(roleDto);
        return responseDto;
    }

    /**
     * 加载用户
     *
     * @param roleId 角色 ID
     * @return 响应
     */
    @GetMapping("/list-user/{roleId}")
    public ResponseDto listUser(@PathVariable String roleId) {
        LOG.info("加载用户开始");
        ResponseDto<List> responseDto = new ResponseDto<>();
        List<String> userIdList = roleService.listUser(roleId);
        responseDto.setContent(userIdList);
        return responseDto;
    }
}