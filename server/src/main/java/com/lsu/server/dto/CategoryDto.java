package com.lsu.server.dto;


import lombok.Data;
import lombok.ToString;

/**
 * @author wsuo
 */
@Data
@ToString
public class CategoryDto {

    /**
     * id
     */
    private String id;

    /**
     * 父id
     */
    private String parent;

    /**
     * 名称
     */
    private String name;

    /**
     * 顺序
     */
    private Integer sort;

}