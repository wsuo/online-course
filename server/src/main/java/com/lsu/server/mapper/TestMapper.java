package com.lsu.server.mapper;

import com.lsu.server.domain.Test;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 持久层接口
 *
 * @author wang suo
 * @version 1.0
 * @date 2020/10/9 0009 20:41
 */
@Mapper
public interface TestMapper {

    /**
     * 获取所有数据
     *
     * @return 数据
     */
    List<Test> getAll();
}
