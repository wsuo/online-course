package com.lsu.server.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.ToString;

import java.util.Date;

@Data
@ToString
public class MemberDto {

    /**
     * id
     */
    private String id;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 密码
     */
    private String password;

    /**
     * 昵称
     */
    private String name;

    /**
     * 头像url
     */
    private String photo;

    /**
     * 注册时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date registerTime;

    /**
     * 图片验证码
     */
    private String imageCode;

    /**
     * 图片验证码 Token
     */
    private String imageCodeToken;
}