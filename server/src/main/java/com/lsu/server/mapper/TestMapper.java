package com.lsu.server.mapper;

import com.lsu.server.domain.Test;
import com.lsu.server.domain.TestExample;

import java.util.List;

import org.apache.ibatis.annotations.Param;

/**
 * @author wsuo
 */
public interface TestMapper {

    /**
     * 计数查询
     *
     * @param example 条件
     * @return 返回数量
     */
    long countByExample(TestExample example);

    /**
     * 根据条件删除
     *
     * @param example 条件
     * @return
     */
    int deleteByExample(TestExample example);

    /**
     * 根据主键删除
     *
     * @param id ID值
     * @return 返回受影响的行数
     */
    int deleteByPrimaryKey(String id);

    /**
     * 插入记录
     *
     * @param record 记录
     * @return 返回插入的行数
     */
    int insert(Test record);

    /**
     * 选择性的插入数据
     *
     * @param record 记录
     * @return 返回插入的行数
     */
    int insertSelective(Test record);

    /**
     * 根据条件查询
     *
     * @param example 条件
     * @return 返回查询结果集
     */
    List<Test> selectByExample(TestExample example);

    /**
     * 根据主键查询
     *
     * @param id 主键 ID
     * @return 返回查询结果
     */
    Test selectByPrimaryKey(String id);

    /**
     * 根据条件有选择性的更新
     *
     * @param record  记录
     * @param example 条件
     * @return 返回受影响的行数
     */
    int updateByExampleSelective(@Param("record") Test record, @Param("example") TestExample example);

    /**
     * 根据条件更新
     *
     * @param record  记录
     * @param example 条件
     * @return
     */
    int updateByExample(@Param("record") Test record, @Param("example") TestExample example);

    /**
     * 根据主键选择性的更新
     *
     * @param record 记录
     * @return 返回受影响的条数
     */
    int updateByPrimaryKeySelective(Test record);

    /**
     * 根据主键更新
     *
     * @param record 记录
     * @return 返回受影响条数
     */
    int updateByPrimaryKey(Test record);
}