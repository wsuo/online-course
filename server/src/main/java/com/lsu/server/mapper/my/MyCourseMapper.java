package com.lsu.server.mapper.my;

import org.apache.ibatis.annotations.Param;

/**
 * 自定义Mapper文件
 *
 * @author wang suo
 * @version 1.0
 * @date 2020/10/14 0014 16:45
 */
public interface MyCourseMapper {
    /**
     * 根据各个小节的时间更新大章时间
     *
     * @param courseId 课程的ID
     * @return 受影响的行数
     */
    int updateTime(@Param("courseId") String courseId);
}
