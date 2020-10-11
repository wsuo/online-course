package com.lsu.business.controller.admin;

import com.lsu.server.dto.ChapterDto;
import com.lsu.server.dto.PageDto;
import com.lsu.server.service.ChapterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 测试
 *
 * @Author wang suo
 * @Date 2020/10/9 0009 18:21
 * @Version 1.0
 */
@RestController
@RequestMapping("/admin/chapter")
public class ChapterController {

    private static final Logger LOG = LoggerFactory.getLogger(ChapterController.class);

    @Resource
    private ChapterService chapterService;

    @PostMapping("/list")
    public PageDto<ChapterDto> chapter(@RequestBody PageDto<ChapterDto> pageDto) {
        chapterService.getAll(pageDto);
        return pageDto;
    }

    @PostMapping("/save")
    public ChapterDto chapter(@RequestBody ChapterDto chapterDto) {
        LOG.info("chapterDto: {}", chapterDto);
        chapterService.save(chapterDto);
        return chapterDto;
    }
}
