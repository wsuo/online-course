package com.lsu.server.dto;

import lombok.Data;
import lombok.ToString;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class SmsDto {

    /**
     * id
     */
    private String id;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 验证码
     */
    private String code;

    /**
     * 用途|枚举[SmsUseEnum]：REGISTER("R", "注册"), FORGET("F", "忘记密码")
     */
    private String use;

    /**
     * 生成时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date at;

    /**
     * 状态|枚举[SmsStatusEnum]：USED("U", "已使用"), NOT_USED("N", "未使用")
     */
    private String status;

}