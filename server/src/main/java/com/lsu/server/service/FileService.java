package com.lsu.server.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lsu.server.domain.File;
import com.lsu.server.domain.FileExample;
import com.lsu.server.dto.FileDto;
import com.lsu.server.dto.PageDto;
import com.lsu.server.mapper.FileMapper;
import com.lsu.server.util.CopyUtil;
import com.lsu.server.util.UuidUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

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
public class FileService {

    @Resource
    private FileMapper fileMapper;

    /**
     * 分页查询
     *
     * @return 数据
     */
    public void getAll(PageDto<FileDto> pageDto) {
        /*
         * 分页插件的使用:
         *   规则是调用 startPage 之后遇到的第一个 select 语句会进行分页;
         * */
        PageHelper.startPage(pageDto.getPage(), pageDto.getSize());
        FileExample fileExample = new FileExample();
        List<File> fileList = fileMapper.selectByExample(fileExample);
        PageInfo<File> pageInfo = new PageInfo<>(fileList);
        pageDto.setTotal(pageInfo.getTotal());
        List<FileDto> fileDtoList = CopyUtil.copyList(fileList, FileDto.class);
        pageDto.setList(fileDtoList);
    }

    /**
     * 根据 ID 是否为空判断是删除还是新增
     *  在查询的时候先去看数据库有没有这个 key ,如果有说明之前传过
     *
     * @param fileDto 数据传输对象
     */
    public void save(FileDto fileDto) {
        File file = CopyUtil.copy(fileDto, File.class);
        File fileDb = selectByKey(fileDto.getKey());
        if (fileDb == null) {
            this.insert(file);
        } else {
            fileDb.setShardIndex(fileDto.getShardIndex());
            this.update(fileDb);
        }
    }

    private void insert(File file) {
        Date now = new Date();
        file.setCreatedAt(now);
        file.setUpdatedAt(now);
        file.setId(UuidUtil.getShortUuid());
        fileMapper.insert(file);
    }

    private void update(File file) {
        file.setUpdatedAt(new Date());
        fileMapper.updateByPrimaryKey(file);
    }

    public void delete(String id) {
        fileMapper.deleteByPrimaryKey(id);
    }

    /**
     * 根据 key 查询记录
     *
     * @param key  KEY值
     * @return 返回文件对象
     */
    public File selectByKey(String key) {
        FileExample example = new FileExample();
        FileExample.Criteria criteria = example.createCriteria();
        criteria.andKeyEqualTo(key);
        List<File> fileList = fileMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(fileList)) {
            return null;
        } else {
            return fileList.get(0);
        }
    }
}
