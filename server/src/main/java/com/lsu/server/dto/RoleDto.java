package com.lsu.server.dto;

import lombok.Data;
import lombok.ToString;

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

}