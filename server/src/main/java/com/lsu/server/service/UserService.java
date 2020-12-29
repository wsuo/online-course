package com.lsu.server.service;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lsu.server.domain.User;
import com.lsu.server.domain.UserExample;
import com.lsu.server.dto.LoginUserDto;
import com.lsu.server.dto.PageDto;
import com.lsu.server.dto.ResourceDto;
import com.lsu.server.dto.UserDto;
import com.lsu.server.exception.BusinessException;
import com.lsu.server.exception.BusinessExceptionCode;
import com.lsu.server.mapper.UserMapper;
import com.lsu.server.mapper.my.MyUserResourceMapper;
import com.lsu.server.util.CopyUtil;
import com.lsu.server.util.UuidUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;

/**
 * 业务层
 *
 * @Author wang suo
 * @Date 2020/10/9 0009 20:49
 * @Version 1.0
 */
@Service
public class UserService {

    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);

    @Resource
    private UserMapper userMapper;

    @Resource
    private MyUserResourceMapper userResourceMapper;

    /**
     * 分页查询
     *
     * @return 数据
     */
    public void getAll(PageDto<UserDto> pageDto) {
        /*
         * 分页插件的使用:
         *   规则是调用 startPage 之后遇到的第一个 select 语句会进行分页;
         * */
        PageHelper.startPage(pageDto.getPage(), pageDto.getSize());
        UserExample userExample = new UserExample();
        List<User> userList = userMapper.selectByExample(userExample);
        PageInfo<User> pageInfo = new PageInfo<>(userList);
        pageDto.setTotal(pageInfo.getTotal());
        List<UserDto> userDtoList = CopyUtil.copyList(userList, UserDto.class);
        pageDto.setList(userDtoList);
    }

    /**
     * 根据 ID 是否为空判断是删除还是新增
     *
     * @param userDto 数据传输对象
     */
    public void save(UserDto userDto) {
        User user = CopyUtil.copy(userDto, User.class);
        if (StringUtils.isEmpty(user.getId())) {
            this.insert(user);
        } else {
            this.update(user);
        }
    }

    private void insert(User user) {
        user.setId(UuidUtil.getShortUuid());
        User userDb = this.selectByLoginName(user.getLoginName());
        if (userDb != null) {
            throw new BusinessException(BusinessExceptionCode.USER_LOGIN_NAME_EXIST);
        }
        userMapper.insert(user);
    }

    /**
     * 更新操作
     * Selective 表示如果字段有值就更新,没值就跳过
     *
     * @param user 用户
     */
    private void update(User user) {
        user.setPassword(null);
        userMapper.updateByPrimaryKeySelective(user);
    }

    public void delete(String id) {
        userMapper.deleteByPrimaryKey(id);
    }

    /**
     * 根据用户名查询用户信息
     *
     * @param loginName 登陆名
     * @return 返回信息
     */
    private User selectByLoginName(String loginName) {
        UserExample example = new UserExample();
        UserExample.Criteria criteria = example.createCriteria();
        criteria.andLoginNameEqualTo(loginName);
        List<User> users = userMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(users)) {
            return null;
        } else {
            return users.get(0);
        }
    }

    /**
     * 重置密码
     *
     * @param userDto user传输对象
     */
    public void savePassword(UserDto userDto) {
        User user = new User();
        user.setId(userDto.getId());
        user.setPassword(userDto.getPassword());
        userMapper.updateByPrimaryKeySelective(user);
    }

    /**
     * 登陆
     *
     * @param userDto 用户对象
     */
    public LoginUserDto login(UserDto userDto) {
        User user = selectByLoginName(userDto.getLoginName());
        if (user == null) {
            // 用户名不存在
            LOG.info("用户名不存在: {}", userDto.getLoginName());
            throw new BusinessException(BusinessExceptionCode.LOGIN_USER_ERROR);
        } else {
            if (user.getPassword().equals(userDto.getPassword())) {
                // 登陆成功
                LoginUserDto loginUserDto = CopyUtil.copy(user, LoginUserDto.class);
                // 为当前用户读取权限
                setAuth(loginUserDto);
                return loginUserDto;
            } else {
                // 密码不对
                LOG.info("密码不对,输入密码: {} 数据库密码: {}", userDto.getPassword(), user.getPassword());
                throw new BusinessException(BusinessExceptionCode.LOGIN_USER_ERROR);
            }
        }
    }

    /**
     * 为登录用户读取权限
     *
     * @param loginUserDto 登陆对象
     */
    private void setAuth(LoginUserDto loginUserDto) {
        List<ResourceDto> resources = userResourceMapper.findResources(loginUserDto.getId());
        loginUserDto.setResources(resources);

        // 整理所有权限的请求: 用于接口拦截
        HashSet<String> requestSet = new HashSet<>();
        if (!CollectionUtils.isEmpty(resources)) {
            for (ResourceDto resource : resources) {
                String arrayString = resource.getRequest();
                List<String> requestList = JSON.parseArray(arrayString, String.class);
                if (!CollectionUtils.isEmpty(requestList)) {
                    requestSet.addAll(requestList);
                }
            }
        }
        LOG.info("该用户有权限的请求：{}", requestSet);
        loginUserDto.setRequests(requestSet);
    }
}
