package com.lsu.server.dto;

import lombok.Data;

import java.util.List;

/**
 * 分页对象
 *
 * @Author wang suo
 * @Date 2020/10/11 0011 8:49
 * @Version 1.0
 */
@Data
public class PageDto<T> {
    /**
     * 当前页数
     */
    private int page;

    /**
     * 每页条数
     */
    private int size;

    /**
     * 总条数
     */
    private long total;

    /**
     * 数据
     */
    private List<T> list;
}
