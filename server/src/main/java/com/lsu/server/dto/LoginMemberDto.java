package com.lsu.server.dto;

import lombok.Data;
import lombok.ToString;

/**
 * 登录会员
 *
 * @Author wang suo
 * @Date 2021/1/2 0002 16:09
 * @Version 1.0
 */
@Data
@ToString
public class LoginMemberDto {
    private String id;

    private String mobile;

    private String name;

    private String token;

    private String photo;
}
