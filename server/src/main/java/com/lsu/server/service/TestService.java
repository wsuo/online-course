package com.lsu.server.service;

import com.lsu.server.domain.Test;
import com.lsu.server.mapper.TestMapper;
import org.springframework.stereotype.Service;

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
public class TestService {

    @Resource
    private TestMapper testMapper;

    /**
     * 获取所有数据
     *
     * @return 数据
     */
    public List<Test> getAll() {
        return testMapper.getAll();
    }
}
