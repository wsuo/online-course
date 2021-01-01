package com.lsu.server.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lsu.server.domain.Teacher;
import com.lsu.server.domain.TeacherExample;
import com.lsu.server.dto.TeacherDto;
import com.lsu.server.dto.PageDto;
import com.lsu.server.mapper.TeacherMapper;
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
public class TeacherService {

    @Resource
    private TeacherMapper teacherMapper;

    /**
     * 分页查询
     *
     * @return 数据
     */
    public void getAll(PageDto<TeacherDto> pageDto) {
        /*
         * 分页插件的使用:
         *   规则是调用 startPage 之后遇到的第一个 select 语句会进行分页;
         * */
        PageHelper.startPage(pageDto.getPage(), pageDto.getSize());
        TeacherExample teacherExample = new TeacherExample();
        List<Teacher> teacherList = teacherMapper.selectByExample(teacherExample);
        PageInfo<Teacher> pageInfo = new PageInfo<>(teacherList);
        pageDto.setTotal(pageInfo.getTotal());
        List<TeacherDto> teacherDtoList = CopyUtil.copyList(teacherList, TeacherDto.class);
        pageDto.setList(teacherDtoList);
    }

    /**
     * 根据 ID 是否为空判断是删除还是新增
     *
     * @param teacherDto 数据传输对象
     */
    public void save(TeacherDto teacherDto) {
        Teacher teacher = CopyUtil.copy(teacherDto, Teacher.class);
        if (StringUtils.isEmpty(teacher.getId())) {
            this.insert(teacher);
        } else {
            this.update(teacher);
        }
    }

    private void insert(Teacher teacher) {
        teacher.setId(UuidUtil.getShortUuid());
        teacherMapper.insert(teacher);
    }

    private void update(Teacher teacher) {
        teacherMapper.updateByPrimaryKey(teacher);
    }

    public void delete(String id) {
        teacherMapper.deleteByPrimaryKey(id);
    }

    /**
     * 获取所有讲师的信息
     *
     * @return 返回讲师集合
     */
    public List<TeacherDto> all() {
        List<Teacher> teacherList = teacherMapper.selectByExample(null);
        return CopyUtil.copyList(teacherList, TeacherDto.class);
    }

    /**
     * 根据 ID 查询
     *
     * @param teacherId 讲师 ID
     * @return 返回讲师对象
     */
    public TeacherDto findById(String teacherId) {
        return CopyUtil.copy(teacherMapper.selectByPrimaryKey(teacherId), TeacherDto.class);
    }
}
