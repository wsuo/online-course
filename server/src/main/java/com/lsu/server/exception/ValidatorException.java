package com.lsu.server.exception;

/**
 * 自定义校验异常
 *
 * @Author wang suo
 * @Date 2020/10/12 0012 8:31
 * @Version 1.0
 */
public class ValidatorException extends RuntimeException {
    public ValidatorException(String message) {
        super(message);
    }
}
