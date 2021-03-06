package com.lsu.business.controller.admin;

import com.lsu.server.dto.TeacherDto;
import com.lsu.server.dto.PageDto;
import com.lsu.server.dto.ResponseDto;
import com.lsu.server.service.TeacherService;
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
@RequestMapping("/admin/teacher")
public class TeacherController {

    private static final Logger LOG = LoggerFactory.getLogger(TeacherController.class);
    public static final String BUSINESS_NAME = "讲师";

    @Resource
    private TeacherService teacherService;

    @PostMapping("/list")
    public ResponseDto<PageDto> getAll(@RequestBody PageDto<TeacherDto> pageDto) {
        ResponseDto<PageDto> responseDto = new ResponseDto<>();
        teacherService.getAll(pageDto);
        responseDto.setContent(pageDto);
        return responseDto;
    }

    @GetMapping("/all")
    public ResponseDto<List> all() {
        ResponseDto<List> responseDto = new ResponseDto<>();
        List<TeacherDto> teacherDtoList = teacherService.all();
        responseDto.setContent(teacherDtoList);
        return responseDto;
    }

    @PostMapping("/save")
    public ResponseDto<TeacherDto> save(@RequestBody TeacherDto teacherDto) {

        // 保存校验
        ValidatorUtil.require(teacherDto.getName(), "姓名");
        ValidatorUtil.length(teacherDto.getName(), "姓名", 1, 50);
        ValidatorUtil.length(teacherDto.getNickname(), "昵称", 1, 50);
        ValidatorUtil.length(teacherDto.getImage(), "头像", 1, 100);
        ValidatorUtil.length(teacherDto.getPosition(), "职位", 1, 50);
        ValidatorUtil.length(teacherDto.getMotto(), "座右铭", 1, 50);
        ValidatorUtil.length(teacherDto.getIntro(), "简介", 1, 500);

        ResponseDto<TeacherDto> responseDto = new ResponseDto<>();
        teacherService.save(teacherDto);
        responseDto.setContent(teacherDto);
        return responseDto;
    }

    @DeleteMapping("/delete/{id}")
    public ResponseDto delete(@PathVariable String id) {
        ResponseDto responseDto = new ResponseDto();
        teacherService.delete(id);
        return responseDto;
    }
}