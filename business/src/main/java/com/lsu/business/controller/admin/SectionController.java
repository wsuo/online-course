package com.lsu.business.controller.admin;

import com.lsu.server.dto.SectionDto;
import com.lsu.server.dto.PageDto;
import com.lsu.server.dto.ResponseDto;
import com.lsu.server.service.SectionService;
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
@RequestMapping("/admin/section")
public class SectionController {

    private static final Logger LOG = LoggerFactory.getLogger(SectionController.class);
    public static final String BUSINESS_NAME = "小节";

    @Resource
    private SectionService sectionService;

    @PostMapping("/list")
    public ResponseDto<PageDto> getAll(@RequestBody PageDto<SectionDto> pageDto) {
        ResponseDto<PageDto> responseDto = new ResponseDto<>();
        sectionService.getAll(pageDto);
        responseDto.setContent(pageDto);
        return responseDto;
    }

    @DeleteMapping("/delete/{id}")
    public ResponseDto delete(@PathVariable String id) {
        ResponseDto responseDto = new ResponseDto();
        sectionService.delete(id);
        return responseDto;
    }
}
