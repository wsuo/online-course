package com.lsu.server.dto;

import lombok.Data;
import lombok.ToString;

/**
 * @author wsuo
 */
@Data
@ToString
public class LoginUserDto {

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
     * 登陆凭证
     */
    private String token;
}