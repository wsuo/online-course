package com.lsu.server.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author wsuo
 */
@Data
@ToString
public class CourseDto {

    /**
     * id
     */
    private String id;

    /**
     * 名称
     */
    private String name;

    /**
     * 概述
     */
    private String summary;

    /**
     * 时长|单位秒
     */
    private Integer time;

    /**
     * 价格（元）
     */
    private BigDecimal price;

    /**
     * 封面
     */
    private String image;

    /**
     * 级别|枚举[CourseLevelEnum]：ONE("1", "初级"),TWO("2", "中级"),THREE("3", "高级")
     */
    private String level;

    /**
     * 收费|枚举[CourseChargeEnum]：CHARGE("C", "收费"),FREE("F", "免费")
     */
    private String charge;

    /**
     * 状态|枚举[CourseStatusEnum]：PUBLISH("P", "发布"),DRAFT("D", "草稿")
     */
    private String status;

    /**
     * 报名数
     */
    private Integer enroll;

    /**
     * 顺序
     */
    private Integer sort;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createdAt;

    /**
     * 修改时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updatedAt;

    private List<CategoryDto> categories;

    /**
     * 讲师的 ID
     */
    private String teacherId;

    /**
     * 课程详情需要所有的大章
     */
    private List<ChapterDto> chapters;

    /**
     * 课程详情需要所有的小节
     */
    private List<SectionDto> sections;

    /**
     * 课程详情需要课程内容
     */
    private String content;

    /**
     * 课程详情需要所有的讲师
     */
    private TeacherDto teacher;
}