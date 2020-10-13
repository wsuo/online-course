package com.lsu.server.enums;

/**
 * @author wsuo
 */

public enum SmsStatusEnum {

    /*
    U 表示 已使用
    N 表示 未使用
     */
    USED("U", "已使用"),
    NOT_USED("N", "未使用");

    private String code;

    private String desc;

    SmsStatusEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }
}
