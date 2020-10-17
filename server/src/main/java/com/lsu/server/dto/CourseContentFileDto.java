package com.lsu.server.dto;

import lombok.Data;
import lombok.ToString;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class CourseContentFileDto {

    /**
     * id
     */
    private String id;

    /**
     * 课程id
     */
    private String courseId;

    /**
     * 地址
     */
    private String url;

    /**
     * 文件名
     */
    private String name;

    /**
     * 大小|字节b
     */
    private Integer size;

}