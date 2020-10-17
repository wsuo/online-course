package com.lsu.business.controller.admin;

import com.lsu.server.dto.CourseContentFileDto;
import com.lsu.server.dto.ResponseDto;
import com.lsu.server.service.CourseContentFileService;
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
@RequestMapping("/admin/course-content-file")
public class CourseContentFileController {

    private static final Logger LOG = LoggerFactory.getLogger(CourseContentFileController.class);
    public static final String BUSINESS_NAME = "课程内容文件";

    @Resource
    private CourseContentFileService courseContentFileService;

    @GetMapping("/list/{courseId}")
    public ResponseDto<List> getAll(@PathVariable String courseId) {
        ResponseDto<List> responseDto = new ResponseDto<>();
        List<CourseContentFileDto> list = courseContentFileService.getAll(courseId);
        responseDto.setContent(list);
        return responseDto;
    }

    @PostMapping("/save")
    public ResponseDto<CourseContentFileDto> save(@RequestBody CourseContentFileDto courseContentFileDto) {

        // 保存校验
        ValidatorUtil.require(courseContentFileDto.getCourseId(), "课程id");
        ValidatorUtil.length(courseContentFileDto.getUrl(), "地址", 1, 100);
        ValidatorUtil.length(courseContentFileDto.getName(), "文件名", 1, 100);

        ResponseDto<CourseContentFileDto> responseDto = new ResponseDto<>();
        courseContentFileService.save(courseContentFileDto);
        responseDto.setContent(courseContentFileDto);
        return responseDto;
    }

    @DeleteMapping("/delete/{id}")
    public ResponseDto delete(@PathVariable String id) {
        ResponseDto responseDto = new ResponseDto();
        courseContentFileService.delete(id);
        return responseDto;
    }
}