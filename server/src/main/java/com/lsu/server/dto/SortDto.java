package com.lsu.server.dto;

import lombok.Data;
import lombok.ToString;

/**
 * 排序数据类型
 *
 * @Author wang suo
 * @Date 2020/10/16 0016 21:54
 * @Version 1.0
 */
@Data
@ToString
public class SortDto {
    /**
     * 课程 ID
     */
    private String id;
    /**
     * 当前排序
     */
    private int oldSort;
    /**
     * 新排序
     */
    private int newSort;
}
