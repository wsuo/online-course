package com.lsu.system.controller.admin;

import com.lsu.server.dto.PageDto;
import com.lsu.server.dto.ResourceDto;
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
    public ResponseDto<ResourceDto> save(@RequestBody String jsonStr) {

        ValidatorUtil.require(jsonStr, "资源");
        ResponseDto<ResourceDto> responseDto = new ResponseDto<>();
        resourceService.saveJson(jsonStr);
        return responseDto;
    }

    @DeleteMapping("/delete/{id}")
    public ResponseDto delete(@PathVariable String id) {
        ResponseDto responseDto = new ResponseDto();
        resourceService.delete(id);
        return responseDto;
    }
}