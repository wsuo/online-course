package com.lsu.business.controller.web;

import com.lsu.server.dto.ResponseDto;
import com.lsu.server.dto.SmsDto;
import com.lsu.server.service.SmsService;
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
@RestController("webSmsController")
@RequestMapping("/web/sms")
public class SmsController {

    private static final Logger LOG = LoggerFactory.getLogger(SmsController.class);
    public static final String BUSINESS_NAME = "短信验证码";

    @Resource
    private SmsService smsService;

    /**
     * 发送短信验证码的接口
     *
     * @param smsDto Sms对象
     * @return 返回响应
     */
    @PostMapping("/send")
    public ResponseDto send(@RequestBody SmsDto smsDto) {
        LOG.info("发送短信请求开始: {}", smsDto);
        ResponseDto responseDto = new ResponseDto();
        smsService.sendCode(smsDto);
        LOG.info("发送短信请求结束");
        return responseDto;
    }
}