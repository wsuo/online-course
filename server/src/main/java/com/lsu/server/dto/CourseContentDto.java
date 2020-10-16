package com.lsu.server.dto;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class CourseContentDto {

    /**
     * 课程id
     */
    private String id;

    /**
     * 课程内容
     */
    private String content;

}