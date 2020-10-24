package com.lsu.server.dto;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class RoleResourceDto {

    /**
     * id
     */
    private String id;

    /**
     * 角色|id
     */
    private String roleId;

    /**
     * 资源|id
     */
    private String resourceId;

}