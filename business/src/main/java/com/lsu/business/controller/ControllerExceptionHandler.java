package com.lsu.business.controller;

import com.lsu.server.dto.ResponseDto;
import com.lsu.server.exception.BusinessException;
import com.lsu.server.exception.ValidatorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 控制器的异常处理器
 *
 * @Author wang suo
 * @Date 2020/10/12 0012 8:54
 * @Version 1.0
 */
@ControllerAdvice
public class ControllerExceptionHandler {

    private static final Logger LOG = LoggerFactory.getLogger(ControllerExceptionHandler.class);

    /**
     * 只要控制器抛异常: 并且异常类型是 ValidatorException 就会被拦截到然后处理
     *
     * @param e
     * @return
     */
    @ResponseBody
    @ExceptionHandler(value = ValidatorException.class)
    public ResponseDto validatorExceptionHandler(ValidatorException e) {
        ResponseDto responseDto = new ResponseDto();
        responseDto.setSuccess(false);
        LOG.info(e.getMessage());
        // 如果直接返回错误信息就会被别人探测出校验规则
        responseDto.setMessage("请求参数异常!");
        return responseDto;
    }

    /**
     * 业务异常的统一处理
     *
     * @param e
     * @return
     */
    @ExceptionHandler(value = BusinessException.class)
    @ResponseBody
    public ResponseDto businessExceptionHandler(BusinessException e) {
        ResponseDto responseDto = new ResponseDto();
        responseDto.setSuccess(false);
        LOG.error("业务异常: {}", e.getCode().getDesc());
        responseDto.setMessage(e.getCode().getDesc());
        return responseDto;
    }
}
