package com.lsu.business.controller.admin;

import com.lsu.server.dto.MemberCourseDto;
import com.lsu.server.dto.PageDto;
import com.lsu.server.dto.ResponseDto;
import com.lsu.server.service.MemberCourseService;
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
@RequestMapping("/admin/memberCourse")
public class MemberCourseController {

    private static final Logger LOG = LoggerFactory.getLogger(MemberCourseController.class);
    public static final String BUSINESS_NAME = "会员课程报名";

    @Resource
    private MemberCourseService memberCourseService;

    @PostMapping("/list")
    public ResponseDto<PageDto> getAll(@RequestBody PageDto<MemberCourseDto> pageDto) {
        ResponseDto<PageDto> responseDto = new ResponseDto<>();
        memberCourseService.getAll(pageDto);
        responseDto.setContent(pageDto);
        return responseDto;
    }

    @PostMapping("/save")
    public ResponseDto<MemberCourseDto> save(@RequestBody MemberCourseDto memberCourseDto) {

        // 保存校验
        ValidatorUtil.require(memberCourseDto.getMemberId(), "会员id");
        ValidatorUtil.require(memberCourseDto.getCourseId(), "课程id");

        ResponseDto<MemberCourseDto> responseDto = new ResponseDto<>();
        memberCourseService.save(memberCourseDto);
        responseDto.setContent(memberCourseDto);
        return responseDto;
    }

    @DeleteMapping("/delete/{id}")
    public ResponseDto delete(@PathVariable String id) {
        ResponseDto responseDto = new ResponseDto();
        memberCourseService.delete(id);
        return responseDto;
    }
}