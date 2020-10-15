package com.lsu.server.dto;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class CourseCategoryDto {

    /**
     * id
     */
    private String id;

    /**
     * 课程|course.id
     */
    private String courseId;

    /**
     * 分类|course.id
     */
    private String categoryId;

}