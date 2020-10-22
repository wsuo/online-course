package com.lsu.server.exception;

/**
 * @author wsuo
 */
public class BusinessException extends RuntimeException{

    private BusinessExceptionCode code;

    public BusinessException (BusinessExceptionCode code) {
        super(code.getDesc());
        this.code = code;
    }

    public BusinessExceptionCode getCode() {
        return code;
    }

    public void setCode(BusinessExceptionCode code) {
        this.code = code;
    }

    /**
     * 重写方法: 不写入堆栈信息，提高性能
     *  也就是不打印报错信息
     */
    @Override
    public Throwable fillInStackTrace() {
        return this;
    }
}