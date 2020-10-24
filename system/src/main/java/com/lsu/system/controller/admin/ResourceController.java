package com.lsu.system.controller.admin;

import com.lsu.server.dto.ResourceDto;
import com.lsu.server.dto.PageDto;
import com.lsu.server.dto.ResponseDto;
import com.lsu.server.service.ResourceService;
import com.lsu.server.util.ValidatorUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 测试
 *
 * @Author wang suo
 * @Date 2020/10/9 0009 18:21
 * @Version 1.0
 */
@RestController
@RequestMapping("/admin/resource")
public class ResourceController {

    private static final Logger LOG = LoggerFactory.getLogger(ResourceController.class);
    public static final String BUSINESS_NAME = "资源";

    @Resource
    private ResourceService resourceService;

    @PostMapping("/list")
    public ResponseDto<PageDto> getAll(@RequestBody PageDto<ResourceDto> pageDto) {
        ResponseDto<PageDto> responseDto = new ResponseDto<>();
        resourceService.getAll(pageDto);
        responseDto.setContent(pageDto);
        return responseDto;
    }

    @PostMapping("/save")
    public ResponseDto<ResourceDto> save(@RequestBody ResourceDto resourceDto) {

        // 保存校验
        ValidatorUtil.require(resourceDto.getName(), "名称");
        ValidatorUtil.length(resourceDto.getName(), "名称", 1, 100);
        ValidatorUtil.length(resourceDto.getPage(), "页面", 1, 50);
        ValidatorUtil.length(resourceDto.getRequest(), "请求", 1, 200);

        ResponseDto<ResourceDto> responseDto = new ResponseDto<>();
        resourceService.save(resourceDto);
        responseDto.setContent(resourceDto);
        return responseDto;
    }

    @DeleteMapping("/delete/{id}")
    public ResponseDto delete(@PathVariable String id) {
        ResponseDto responseDto = new ResponseDto();
        resourceService.delete(id);
        return responseDto;
    }
}