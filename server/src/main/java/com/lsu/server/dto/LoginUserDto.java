package com.lsu.server.dto;

import lombok.Data;
import lombok.ToString;

import java.util.HashSet;
import java.util.List;

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

    /**
     * 所有资源: 用于前端界面控制
     */
    private List<ResourceDto> resources;

    /**
     *  所有资源请求: 用于拦截接口请求
     */
    private HashSet<String> requests;
}