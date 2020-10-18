package com.lsu.server.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lsu.server.domain.Section;
import com.lsu.server.domain.SectionExample;
import com.lsu.server.domain.SectionPageDto;
import com.lsu.server.dto.SectionDto;
import com.lsu.server.enums.SectionChargeEnum;
import com.lsu.server.mapper.SectionMapper;
import com.lsu.server.util.CopyUtil;
import com.lsu.server.util.UuidUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * 业务层
 *
 * @Author wang suo
 * @Date 2020/10/9 0009 20:49
 * @Version 1.0
 */
@Service
public class SectionService {

    @Resource
    private SectionMapper sectionMapper;

    @Resource
    private CourseService courseService;

    /**
     * 分页查询
     *
     * @return 数据
     */
    public void getAll(SectionPageDto<SectionDto> sectionPageDto) {
        /*
         * 分页插件的使用:
         *   规则是调用 startPage 之后遇到的第一个 select 语句会进行分页;
         * */
        PageHelper.startPage(sectionPageDto.getPage(), sectionPageDto.getSize());
        SectionExample sectionExample = new SectionExample();

        /*
        增加 where 查询条件
         */
        SectionExample.Criteria criteria = sectionExample.createCriteria();
        if (!StringUtils.isEmpty(sectionPageDto.getCourseId())) {
            criteria.andCourseIdEqualTo(sectionPageDto.getCourseId());
        }
        if (!StringUtils.isEmpty(sectionPageDto.getChapterId())) {
            criteria.andChapterIdEqualTo(sectionPageDto.getChapterId());
        }

        sectionExample.setOrderByClause("sort asc");
        List<Section> sectionList = sectionMapper.selectByExample(sectionExample);
        PageInfo<Section> pageInfo = new PageInfo<>(sectionList);
        sectionPageDto.setTotal(pageInfo.getTotal());
        List<SectionDto> sectionDtoList = CopyUtil.copyList(sectionList, SectionDto.class);
        sectionPageDto.setList(sectionDtoList);
    }

    /**
     * 根据 ID 是否为空判断是删除还是新增
     *  如果抛出的异常是 Exception 就会回滚
     * @param sectionDto 数据传输对象
     */
    @Transactional(rollbackFor = Exception.class)
    public void save(SectionDto sectionDto) {
        Section section = CopyUtil.copy(sectionDto, Section.class);
        if (StringUtils.isEmpty(section.getId())) {
            this.insert(section);
        } else {
            this.update(section);
        }
        courseService.updateTime(sectionDto.getCourseId());
    }

    private void insert(Section section) {
        Date now = new Date();
        section.setCreatedAt(now);
        section.setUpdatedAt(now);
        section.setId(UuidUtil.getShortUuid());
        section.setCharge(SectionChargeEnum.CHARGE.getCode());
        sectionMapper.insert(section);
    }

    private void update(Section section) {
        section.setUpdatedAt(new Date());
        sectionMapper.updateByPrimaryKey(section);
    }

    public void delete(String id) {
        sectionMapper.deleteByPrimaryKey(id);
    }
}
