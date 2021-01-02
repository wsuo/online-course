package com.lsu.server.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lsu.server.domain.Member;
import com.lsu.server.domain.MemberExample;
import com.lsu.server.dto.LoginMemberDto;
import com.lsu.server.dto.MemberDto;
import com.lsu.server.dto.PageDto;
import com.lsu.server.exception.BusinessException;
import com.lsu.server.exception.BusinessExceptionCode;
import com.lsu.server.mapper.MemberMapper;
import com.lsu.server.util.CopyUtil;
import com.lsu.server.util.UuidUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
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
public class MemberService {

    private static final Logger LOG = LoggerFactory.getLogger(MemberService.class);

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

    /**
     * 按手机号查找
     * @param mobile
     * @return
     */
    public MemberDto findByMobile(String mobile) {
        Member member = this.selectByMobile(mobile);
        return CopyUtil.copy(member, MemberDto.class);
    }

    /**
     * 按手机号查找
     * @param mobile
     * @return
     */
    public Member selectByMobile(String mobile) {
        if (StringUtils.isEmpty(mobile)) {
            return null;
        }
        MemberExample example = new MemberExample();
        example.createCriteria().andMobileEqualTo(mobile);
        List<Member> memberList = memberMapper.selectByExample(example);
        if (memberList == null || memberList.size() == 0) {
            return null;
        } else {
            return memberList.get(0);
        }

    }

    /**
     * 登录
     * @param memberDto
     */
    public LoginMemberDto login(MemberDto memberDto) {
        Member member = selectByMobile(memberDto.getMobile());
        if (member == null) {
            LOG.info("手机号不存在, {}", memberDto.getMobile());
            throw new BusinessException(BusinessExceptionCode.LOGIN_MEMBER_ERROR);
        } else {
            if (member.getPassword().equals(memberDto.getPassword())) {
                // 登录成功
                return CopyUtil.copy(member, LoginMemberDto.class);
            } else {
                LOG.info("密码不对, 输入密码：{}, 数据库密码：{}", memberDto.getPassword(), member.getPassword());
                throw new BusinessException(BusinessExceptionCode.LOGIN_MEMBER_ERROR);
            }
        }
    }

}
