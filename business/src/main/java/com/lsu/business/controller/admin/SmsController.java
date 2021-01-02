package com.lsu.business.controller.admin;

import com.lsu.server.dto.SmsDto;
import com.lsu.server.dto.PageDto;
import com.lsu.server.dto.ResponseDto;
import com.lsu.server.service.SmsService;
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

    @PostMapping("/save")
    public ResponseDto<SmsDto> save(@RequestBody SmsDto smsDto) {

        // 保存校验
        ValidatorUtil.require(smsDto.getMobile(), "手机号");
        ValidatorUtil.length(smsDto.getMobile(), "手机号", 1, 50);
        ValidatorUtil.require(smsDto.getCode(), "验证码");
        ValidatorUtil.require(smsDto.getUse(), "用途");
        ValidatorUtil.require(smsDto.getStatus(), "状态");

        ResponseDto<SmsDto> responseDto = new ResponseDto<>();
        smsService.save(smsDto);
        responseDto.setContent(smsDto);
        return responseDto;
    }

    @DeleteMapping("/delete/{id}")
    public ResponseDto delete(@PathVariable String id) {
        ResponseDto responseDto = new ResponseDto();
        smsService.delete(id);
        return responseDto;
    }
}