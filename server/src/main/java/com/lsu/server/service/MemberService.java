package com.lsu.server.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lsu.server.domain.Member;
import com.lsu.server.domain.MemberExample;
import com.lsu.server.dto.MemberDto;
import com.lsu.server.dto.PageDto;
import com.lsu.server.mapper.MemberMapper;
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
public class MemberService {

    @Resource
    private MemberMapper memberMapper;

    /**
     * 分页查询
     *
     * @return 数据
     */
    public void getAll(PageDto<MemberDto> pageDto) {
        /*
         * 分页插件的使用:
         *   规则是调用 startPage 之后遇到的第一个 select 语句会进行分页;
         * */
        PageHelper.startPage(pageDto.getPage(), pageDto.getSize());
        MemberExample memberExample = new MemberExample();
        List<Member> memberList = memberMapper.selectByExample(memberExample);
        PageInfo<Member> pageInfo = new PageInfo<>(memberList);
        pageDto.setTotal(pageInfo.getTotal());
        List<MemberDto> memberDtoList = CopyUtil.copyList(memberList, MemberDto.class);
        pageDto.setList(memberDtoList);
    }

    /**
     * 根据 ID 是否为空判断是删除还是新增
     *
     * @param memberDto 数据传输对象
     */
    public void save(MemberDto memberDto) {
        Member member = CopyUtil.copy(memberDto, Member.class);
        if (StringUtils.isEmpty(member.getId())) {
            this.insert(member);
        } else {
            this.update(member);
        }
    }

    private void insert(Member member) {
        Date now = new Date();
        member.setId(UuidUtil.getShortUuid());
        // 设置注册时间
        member.setRegisterTime(now);
        memberMapper.insert(member);
    }

    private void update(Member member) {
        memberMapper.updateByPrimaryKey(member);
    }

    public void delete(String id) {
        memberMapper.deleteByPrimaryKey(id);
    }
}
