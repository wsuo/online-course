package com.lsu.business.controller.web;

import com.lsu.server.dto.CourseDto;
import com.lsu.server.dto.PageDto;
import com.lsu.server.dto.ResponseDto;
import com.lsu.server.service.CourseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * web端课程
 *
 * @Author wang suo
 * @Date 2020/12/29 0029 15:27
 * @Version 1.0
 */
@RestController("webCourseController")
@RequestMapping("/web/course")
public class CourseController {

    private static final Logger LOG = LoggerFactory.getLogger(com.lsu.business.controller.web.CourseController.class);
    public static final String BUSINESS_NAME = "课程";

    @Resource
    private CourseService courseService;

    /**
     * 列表查询: 查询最新的 3 门已发布的课程
     * @return
     */
    @GetMapping("/list-new")
    public ResponseDto<List> listNew() {
        PageDto pageDto = new PageDto();
        pageDto.setPage(1);
        // 设置查询 3 门课程
        pageDto.setSize(3);
        ResponseDto<List> responseDto = new ResponseDto<>();
        List<CourseDto> courseDtos = courseService.listNew(pageDto);
        responseDto.setContent(courseDtos);
        return responseDto;
    }
}