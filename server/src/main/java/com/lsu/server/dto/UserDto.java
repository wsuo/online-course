package com.lsu.server.dto;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class UserDto {

    /**
     * id
     */
    private String id;

    /**
     * 登陆名
     */
    private String loginName;

    /**
     * 昵称
     */
    private String name;

    /**
     * 密码
     */
    private String password;

    /**
     * 验证码
     */
    private String imageCode;

    /**
     * 图片验证码 token
     */
    private String imageCodeToken;
}