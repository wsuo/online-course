package com.lsu.server.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lsu.server.domain.Chapter;
import com.lsu.server.dto.ChapterDto;
import com.lsu.server.dto.PageDto;
import com.lsu.server.mapper.ChapterMapper;
import com.lsu.server.util.UuidUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 业务层
 *
 * @Author wang suo
 * @Date 2020/10/9 0009 20:49
 * @Version 1.0
 */
@Service
public class ChapterService {

    @Resource
    private ChapterMapper chapterMapper;

    /**
     * 分页查询
     *
     * @return 数据
     */
    public void getAll(PageDto<ChapterDto> pageDto) {
        /*
        * 分页插件的使用:
        *   规则是调用 startPage 之后遇到的第一个 select 语句会进行分页;
        * */
        PageHelper.startPage(pageDto.getPage(), pageDto.getSize());
        List<Chapter> chapterList = chapterMapper.selectByExample(null);
        PageInfo<Chapter> pageInfo = new PageInfo<>(chapterList);
        pageDto.setTotal(pageInfo.getTotal());
        ArrayList<ChapterDto> chapterDtoList = new ArrayList<>();
        for (Chapter chapter : chapterList) {
            ChapterDto chapterDto = new ChapterDto();
            BeanUtils.copyProperties(chapter, chapterDto);
            chapterDtoList.add(chapterDto);
        }
        pageDto.setList(chapterDtoList);
    }

    public void save(ChapterDto chapterDto) {
        chapterDto.setId(UuidUtil.getShortUuid());
        Chapter chapter = new Chapter();
        BeanUtils.copyProperties(chapterDto, chapter);
        chapterMapper.insert(chapter);
    }

}
