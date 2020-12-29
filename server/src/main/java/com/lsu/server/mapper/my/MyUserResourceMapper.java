package com.lsu.server.mapper.my;

import com.lsu.server.dto.ResourceDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 自定义 Mapper 文件: 查询用户资源
 *
 * @author wang suo
 * @version 1.0
 * @date 2020/10/14 0014 16:45
 */
public interface MyUserResourceMapper {

    /**
     * 查询指定用户下的所有资源
     *
     * @param userId 用户ID
     * @return 返回资源列表
     */
    List<ResourceDto> findResources(@Param("userId") String userId);
}
