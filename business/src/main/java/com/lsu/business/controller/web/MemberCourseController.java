package com.lsu.business.controller.web;

import com.lsu.server.dto.MemberCourseDto;
import com.lsu.server.dto.ResponseDto;
import com.lsu.server.service.MemberCourseService;
import com.lsu.server.util.ValidatorUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 会员课程控制器
 *
 * @Author wang suo
 * @Date 2021/1/2 0002 20:39
 * @Version 1.0
 */
@RestController("webMemberCourseController")
@RequestMapping("/web/member-course")
public class MemberCourseController {
    private static final Logger LOG = LoggerFactory.getLogger(MemberCourseController.class);
    public static final String BUSINESS_NAME = "WEB会员课程报名";

    @Resource
    private MemberCourseService memberCourseService;

    @PostMapping("/enroll")
    public ResponseDto<MemberCourseDto> enroll(@RequestBody MemberCourseDto memberCourseDto) {

        // 保存校验
        ValidatorUtil.require(memberCourseDto.getMemberId(), "会员id");
        ValidatorUtil.require(memberCourseDto.getCourseId(), "课程id");

        ResponseDto<MemberCourseDto> responseDto = new ResponseDto<>();
        memberCourseDto = memberCourseService.enroll(memberCourseDto);
        responseDto.setContent(memberCourseDto);
        return responseDto;
    }
}
