package com.lsu.business.controller.admin;

import com.lsu.server.dto.CategoryDto;
import com.lsu.server.dto.ResponseDto;
import com.lsu.server.service.CategoryService;
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
@RequestMapping("/admin/category")
public class CategoryController {

    private static final Logger LOG = LoggerFactory.getLogger(CategoryController.class);
    public static final String BUSINESS_NAME = "分类";

    @Resource
    private CategoryService categoryService;

    @PostMapping("/list")
    public ResponseDto<List> getAll() {
        ResponseDto<List> responseDto = new ResponseDto<>();
        List<CategoryDto> categoryDtoList = categoryService.getAll();
        responseDto.setContent(categoryDtoList);
        return responseDto;
    }

    @PostMapping("/save")
    public ResponseDto<CategoryDto> save(@RequestBody CategoryDto categoryDto) {

        // 保存校验
        ValidatorUtil.require(categoryDto.getParent(), "父id");
        ValidatorUtil.require(categoryDto.getName(), "名称");
        ValidatorUtil.length(categoryDto.getName(), "名称", 1, 50);

        ResponseDto<CategoryDto> responseDto = new ResponseDto<>();
        categoryService.save(categoryDto);
        responseDto.setContent(categoryDto);
        return responseDto;
    }

    @DeleteMapping("/delete/{id}")
    public ResponseDto delete(@PathVariable String id) {
        ResponseDto responseDto = new ResponseDto();
        categoryService.delete(id);
        return responseDto;
    }
}