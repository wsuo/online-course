package com.lsu.server.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lsu.server.domain.Course;
import com.lsu.server.domain.CourseContent;
import com.lsu.server.domain.CourseExample;
import com.lsu.server.dto.*;
import com.lsu.server.enums.CourseStatusEnum;
import com.lsu.server.mapper.CourseContentMapper;
import com.lsu.server.mapper.CourseMapper;
import com.lsu.server.mapper.my.MyCourseMapper;
import com.lsu.server.util.CopyUtil;
import com.lsu.server.util.UuidUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class CourseService {

    private static final Logger LOG = LoggerFactory.getLogger(CourseService.class);

    @Resource
    private CourseMapper courseMapper;

    @Resource
    private MyCourseMapper myCourseMapper;

    @Resource
    private CourseCategoryService courseCategoryService;

    @Resource
    private CourseContentMapper courseContentMapper;

    @Resource
    private TeacherService teacherService;

    @Resource
    private ChapterService chapterService;

    @Resource
    private SectionService sectionService;

    /**
     * 分页查询
     *
     * @return 数据
     */
    public void getAll(CoursePageDto pageDto) {
        /*
         * 分页插件的使用:
         *   规则是调用 startPage 之后遇到的第一个 select 语句会进行分页;
         * */
        PageHelper.startPage(pageDto.getPage(), pageDto.getSize());
        List<CourseDto> courseList = myCourseMapper.list(pageDto);
        PageInfo<CourseDto> pageInfo = new PageInfo<>(courseList);
        pageDto.setTotal(pageInfo.getTotal());
        pageDto.setList(courseList);
    }

    /**
     * 根据 ID 是否为空判断是删除还是新增
     *
     * @param courseDto 数据传输对象
     */
    @Transactional(rollbackFor = Exception.class)
    public void save(CourseDto courseDto) {
        Course course = CopyUtil.copy(courseDto, Course.class);
        if (StringUtils.isEmpty(course.getId())) {
            this.insert(course);
        } else {
            this.update(course);
        }

        // 批量保存分类
        courseCategoryService.saveBatch(course.getId(), courseDto.getCategories());
    }

    private void insert(Course course) {
        Date now = new Date();
        course.setCreatedAt(now);
        course.setUpdatedAt(now);
        course.setId(UuidUtil.getShortUuid());
        courseMapper.insert(course);
    }

    private void update(Course course) {
        course.setUpdatedAt(new Date());
        courseMapper.updateByPrimaryKey(course);
    }

    public void delete(String id) {
        courseMapper.deleteByPrimaryKey(id);
    }

    /**
     * 更新视频时长
     *
     * @param courseId 课程 ID
     */
    void updateTime(String courseId) {
        LOG.info("更新视频时长: {}", courseId);
        myCourseMapper.updateTime(courseId);
    }

    /**
     * 列出分类
     *
     * @param courseId 课程 ID
     * @return 返回课程分类列表
     */
    public List<CourseCategoryDto> listCategory(String courseId) {
        return courseCategoryService.listByCourse(courseId);
    }

    /**
     * 查找课程内容
     *
     * @param id 课程 ID
     * @return 返回课程内容
     */
    public CourseContentDto findContent(String id) {
        CourseContent content = courseContentMapper.selectByPrimaryKey(id);
        if (content == null) {
            return null;
        }
        return CopyUtil.copy(content, CourseContentDto.class);
    }

    /**
     * 保存课程内容
     *
     * @param contentDto 课程内容
     * @return 返回保存的条数
     */
    public int saveContent(CourseContentDto contentDto) {
        CourseContent content = CopyUtil.copy(contentDto, CourseContent.class);
        /*
        如果更新不到: 说明表中没有,直接更新即可;
        如果更新到了: 则直接返回结果.
         */
        int i = courseContentMapper.updateByPrimaryKeyWithBLOBs(content);
        if (i == 0) {
            i = courseContentMapper.insert(content);
        }
        return i;
    }

    /**
     * 排序
     *
     * @param sortDto 排序类
     */
    @Transactional(rollbackFor = Exception.class)
    public void sort(SortDto sortDto) {

        // 修改当前记录的排序值
        myCourseMapper.updateSort(sortDto);

        // 如果排序值变大
        if (sortDto.getNewSort() > sortDto.getOldSort()) {
            myCourseMapper.moveSortsForward(sortDto);
        }

        // 如果排序值变小
        if (sortDto.getNewSort() < sortDto.getOldSort()) {
            myCourseMapper.moveSortsBackward(sortDto);
        }
    }

    /**
     * 新课列表查询: 只查询已发布的: 按创建日期倒序
     *
     * @param pageDto 分页对象
     * @return 返回列表
     */
    public List<CourseDto> listNew(PageDto pageDto) {
        PageHelper.startPage(pageDto.getPage(), pageDto.getSize());
        CourseExample example = new CourseExample();
        // 查询的只能是已发布的
        example.createCriteria().andStatusEqualTo(CourseStatusEnum.PUBLISH.getCode());
        // 按照发布的时间倒序
        example.setOrderByClause("created_at desc");
        List<Course> courses = courseMapper.selectByExample(example);
        return CopyUtil.copyList(courses, CourseDto.class);
    }

    /**
     * 新课列表查询: 只查询已发布的: 按报名人数倒序
     *
     * @param pageDto 分页对象
     * @return 返回列表
     */
    public List<CourseDto> listHot(PageDto pageDto) {
        PageHelper.startPage(pageDto.getPage(), pageDto.getSize());
        CourseExample example = new CourseExample();
        // 查询的只能是已发布的
        example.createCriteria().andStatusEqualTo(CourseStatusEnum.PUBLISH.getCode());
        // 按照报名人数倒序
        example.setOrderByClause("enroll desc");
        List<Course> courses = courseMapper.selectByExample(example);
        return CopyUtil.copyList(courses, CourseDto.class);
    }

    /**
     * 根据 ID 查询课程: 供 web 模块使用: 只能查询已发布的
     * @param id ID 值
     * @return 返回课程 Dto
     */
    public CourseDto findCourseById(String id) {
        Course course = courseMapper.selectByPrimaryKey(id);
        if (course == null || !CourseStatusEnum.PUBLISH.getCode().equals(course.getStatus())) {
            return null;
        }
        CourseDto courseDto = CopyUtil.copy(course, CourseDto.class);

        // 查询内容信息
        CourseContent courseContent = courseContentMapper.selectByPrimaryKey(id);
        if (courseContent != null) {
            courseDto.setContent(courseContent.getContent());
        }

        // 查询讲师
        TeacherDto teacherDto = teacherService.findById(courseDto.getTeacherId());
        courseDto.setTeacher(teacherDto);

        // 查找大章
        List<ChapterDto> chapterDtos = chapterService.listByCourse(id);
        courseDto.setChapters(chapterDtos);

        // 查找小节
        List<SectionDto> sectionDtos = sectionService.listByCourse(id);
        courseDto.setSections(sectionDtos);

        return courseDto;
    }
}
