package com.lsu.business.controller.admin;

import com.lsu.server.dto.MemberDto;
import com.lsu.server.dto.PageDto;
import com.lsu.server.dto.ResponseDto;
import com.lsu.server.service.MemberService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 测试
 *
 * @Author wang suo
 * @Date 2020/10/9 0009 18:21
 * @Version 1.0
 */
@RestController
@RequestMapping("/admin/member")
public class MemberController {

    private static final Logger LOG = LoggerFactory.getLogger(MemberController.class);
    public static final String BUSINESS_NAME = "会员";

    @Resource
    private MemberService memberService;

    @PostMapping("/list")
    public ResponseDto<PageDto> getAll(@RequestBody PageDto<MemberDto> pageDto) {
        ResponseDto<PageDto> responseDto = new ResponseDto<>();
        memberService.getAll(pageDto);
        responseDto.setContent(pageDto);
        return responseDto;
    }
}