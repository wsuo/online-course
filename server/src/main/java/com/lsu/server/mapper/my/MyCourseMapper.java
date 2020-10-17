package com.lsu.server.mapper.my;

import com.lsu.server.dto.SortDto;
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

    /**
     * 更新目标的排序值
     *
     * @param sortDto 排序对象
     */
    void updateSort(SortDto sortDto);

    /**
a
     *
     * @param sortDto 排序对象
     */
    void moveSortsForward(SortDto sortDto);

    /**
     * 排序值变小:
     * 要将区间内的排序值+1
     *
     * @param sortDto
     */
    void moveSortsBackward(SortDto sortDto);
}
