package com.lsu.business.controller.admin;

import com.lsu.server.dto.CourseCategoryDto;
import com.lsu.server.dto.CourseDto;
import com.lsu.server.dto.PageDto;
import com.lsu.server.dto.ResponseDto;
import com.lsu.server.service.CourseService;
import com.lsu.server.util.ValidatorUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 测试
 *
 * @Author wang suo
 * @Date 2020/10/9 0009 18:21
 * @Version 1.0
 */
@RestController
@RequestMapping("/admin/course")
public class CourseController {

    private static final Logger LOG = LoggerFactory.getLogger(CourseController.class);
    public static final String BUSINESS_NAME = "课程";

    @Resource
    private CourseService courseService;

    @PostMapping("/list")
    public ResponseDto<PageDto> getAll(@RequestBody PageDto<CourseDto> pageDto) {
        ResponseDto<PageDto> responseDto = new ResponseDto<>();
        courseService.getAll(pageDto);
        responseDto.setContent(pageDto);
        return responseDto;
    }

    @PostMapping("/save")
    public ResponseDto<CourseDto> save(@RequestBody CourseDto courseDto) {

        // 保存校验
        ValidatorUtil.require(courseDto.getName(), "名称");
        ValidatorUtil.length(courseDto.getName(), "名称", 1, 50);
        ValidatorUtil.length(courseDto.getSummary(), "概述", 1, 2000);
        ValidatorUtil.length(courseDto.getImage(), "封面", 1, 100);

        ResponseDto<CourseDto> responseDto = new ResponseDto<>();
        courseService.save(courseDto);
        responseDto.setContent(courseDto);
        return responseDto;
    }

    @DeleteMapping("/delete/{id}")
    public ResponseDto delete(@PathVariable String id) {
        ResponseDto responseDto = new ResponseDto();
        courseService.delete(id);
        return responseDto;
    }

    /**
     * 查询课程下的所有分类
     *
     * @param courseId 课程 ID
     * @return 返回列表
     */
    @PostMapping("/list-category/{courseId}")
    public ResponseDto<List> listCategory(@PathVariable(value = "courseId") String courseId) {
        ResponseDto<List> responseDto = new ResponseDto<>();
        List<CourseCategoryDto> courseCategoryDto = courseService.listCategory(courseId);
        responseDto.setContent(courseCategoryDto);
        /*
        时间 2020年10月15日22:39:47
        responseDto 为空
         */
        LOG.info("responseDto = {}", responseDto);
        return responseDto;
    }
}