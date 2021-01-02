package com.lsu.server.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lsu.server.domain.Sms;
import com.lsu.server.domain.SmsExample;
import com.lsu.server.dto.SmsDto;
import com.lsu.server.dto.PageDto;
import com.lsu.server.mapper.SmsMapper;
import com.lsu.server.util.CopyUtil;
import com.lsu.server.util.UuidUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Date;

/**
 * 业务层
 *
 * @Author wang suo
 * @Date 2020/10/9 0009 20:49
 * @Version 1.0
 */
@Service
public class SmsService {

    @Resource
    private SmsMapper smsMapper;

    /**
     * 分页查询
     *
     * @return 数据
     */
    public void getAll(PageDto<SmsDto> pageDto) {
        /*
         * 分页插件的使用:
         *   规则是调用 startPage 之后遇到的第一个 select 语句会进行分页;
         * */
        PageHelper.startPage(pageDto.getPage(), pageDto.getSize());
        SmsExample smsExample = new SmsExample();
        List<Sms> smsList = smsMapper.selectByExample(smsExample);
        PageInfo<Sms> pageInfo = new PageInfo<>(smsList);
        pageDto.setTotal(pageInfo.getTotal());
        List<SmsDto> smsDtoList = CopyUtil.copyList(smsList, SmsDto.class);
        pageDto.setList(smsDtoList);
    }

    /**
     * 根据 ID 是否为空判断是删除还是新增
     *
     * @param smsDto 数据传输对象
     */
    public void save(SmsDto smsDto) {
        Sms sms = CopyUtil.copy(smsDto, Sms.class);
        if (StringUtils.isEmpty(sms.getId())) {
            this.insert(sms);
        } else {
            this.update(sms);
        }
    }

    private void insert(Sms sms) {
        Date now = new Date();
        sms.setId(UuidUtil.getShortUuid());
        smsMapper.insert(sms);
    }

    private void update(Sms sms) {
        smsMapper.updateByPrimaryKey(sms);
    }

    public void delete(String id) {
        smsMapper.deleteByPrimaryKey(id);
    }
}
