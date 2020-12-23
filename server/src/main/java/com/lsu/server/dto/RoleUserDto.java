package com.lsu.server.dto;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class RoleUserDto {

    /**
     * id
     */
    private String id;

    /**
     * 角色|id
     */
    private String roleId;

    /**
     * 用户|id
     */
    private String userId;

}