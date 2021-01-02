package com.lsu.business.controller.admin;

import com.lsu.server.dto.PageDto;
import com.lsu.server.dto.ResponseDto;
import com.lsu.server.dto.SmsDto;
import com.lsu.server.service.SmsService;
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
@RequestMapping("/admin/sms")
public class SmsController {

    private static final Logger LOG = LoggerFactory.getLogger(SmsController.class);
    public static final String BUSINESS_NAME = "短信验证码";

    @Resource
    private SmsService smsService;

    @PostMapping("/list")
    public ResponseDto<PageDto> getAll(@RequestBody PageDto<SmsDto> pageDto) {
        ResponseDto<PageDto> responseDto = new ResponseDto<>();
        smsService.getAll(pageDto);
        responseDto.setContent(pageDto);
        return responseDto;
    }
}