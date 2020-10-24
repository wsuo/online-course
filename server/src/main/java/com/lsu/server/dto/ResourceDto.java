package com.lsu.server.dto;

import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * @author wsuo
 */
@Data
@ToString
public class ResourceDto {

    /**
     * id
     */
    private String id;

    /**
     * 名称|菜单或按钮
     */
    private String name;

    /**
     * 页面|路由
     */
    private String page;

    /**
     * 请求|接口
     */
    private String request;

    /**
     * 父id
     */
    private String parent;


    /**
     * 子节点
     */
    private List<ResourceDto> children;
}