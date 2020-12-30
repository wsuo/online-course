package com.lsu.server.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 课程分页类
 *
 * @Author wang suo
 * @Date 2020/12/30 0030 19:42
 * @Version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ToString
public class CoursePageDto extends PageDto {
    private String status;
}
