package com.lsu.server.service;

import com.lsu.server.domain.CourseContentFile;
import com.lsu.server.domain.CourseContentFileExample;
import com.lsu.server.dto.CourseContentFileDto;
import com.lsu.server.mapper.CourseContentFileMapper;
import com.lsu.server.util.CopyUtil;
import com.lsu.server.util.UuidUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * 业务层
 *
 * @Author wang suo
 * @Date 2020/10/9 0009 20:49
 * @Version 1.0
 */
@Service
public class CourseContentFileService {

    @Resource
    private CourseContentFileMapper courseContentFileMapper;

    /**
     * 查询所有
     *
     * @return 数据
     */
    public List<CourseContentFileDto> getAll(String courseId) {
        CourseContentFileExample courseContentFileExample = new CourseContentFileExample();
        CourseContentFileExample.Criteria criteria = courseContentFileExample.createCriteria();
        criteria.andCourseIdEqualTo(courseId);
        List<CourseContentFile> courseContentFiles = courseContentFileMapper.selectByExample(courseContentFileExample);
        return CopyUtil.copyList(courseContentFiles, CourseContentFileDto.class);
    }

    /**
     * 根据 ID 是否为空判断是删除还是新增
     *
     * @param courseContentFileDto 数据传输对象
     */
    public void save(CourseContentFileDto courseContentFileDto) {
        CourseContentFile courseContentFile = CopyUtil.copy(courseContentFileDto, CourseContentFile.class);
        if (StringUtils.isEmpty(courseContentFile.getId())) {
            this.insert(courseContentFile);
        } else {
            this.update(courseContentFile);
        }
    }

    private void insert(CourseContentFile courseContentFile) {
        courseContentFile.setId(UuidUtil.getShortUuid());
        courseContentFileMapper.insert(courseContentFile);
    }

    private void update(CourseContentFile courseContentFile) {
        courseContentFileMapper.updateByPrimaryKey(courseContentFile);
    }

    public void delete(String id) {
        courseContentFileMapper.deleteByPrimaryKey(id);
    }
}
