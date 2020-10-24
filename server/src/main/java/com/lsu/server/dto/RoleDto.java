package com.lsu.server.dto;

import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * @author wsuo
 */
@Data
@ToString
public class RoleDto {

    /**
     * id
     */
    private String id;

    /**
     * 角色
     */
    private String name;

    /**
     * 描述
     */
    private String desc;

    /**
     * 这个权限对应的资源们
     */
    private List<String> resourceIds;
}