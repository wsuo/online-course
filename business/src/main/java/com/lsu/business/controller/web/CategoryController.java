package com.lsu.business.controller.web;

import com.lsu.server.dto.CategoryDto;
import com.lsu.server.dto.ResponseDto;
import com.lsu.server.service.CategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 分类控制器
 *
 * @Author wang suo
 * @Date 2020/12/30 0030 21:11
 * @Version 1.0
 */
@RestController("webCategoryController")
@RequestMapping("/web/category")
public class CategoryController {
    private static final Logger LOG = LoggerFactory.getLogger(com.lsu.business.controller.web.CategoryController.class);
    public static final String BUSINESS_NAME = "分类";

    @Resource
    private CategoryService categoryService;

    /**
     * 列表查询
     *
     * @return 返回列表
     */
    @PostMapping("/all")
    public ResponseDto<List> getAll() {
        ResponseDto<List> responseDto = new ResponseDto<>();
        List<CategoryDto> categoryDtoList = categoryService.getAll();
        responseDto.setContent(categoryDtoList);
        return responseDto;
    }
}
