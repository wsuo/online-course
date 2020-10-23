package com.lsu.system.controller.admin;

import com.lsu.server.dto.*;
import com.lsu.server.service.UserService;
import com.lsu.server.util.ValidatorUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 测试
 *
 * @Author wang suo
 * @Date 2020/10/9 0009 18:21
 * @Version 1.0
 */
@RestController
@RequestMapping("/admin/user")
public class UserController {

    private static final Logger LOG = LoggerFactory.getLogger(UserController.class);
    public static final String BUSINESS_NAME = "用户";

    @Resource
    private UserService userService;

    @PostMapping("/list")
    public ResponseDto<PageDto> getAll(@RequestBody PageDto<UserDto> pageDto) {
        ResponseDto<PageDto> responseDto = new ResponseDto<>();
        userService.getAll(pageDto);
        responseDto.setContent(pageDto);
        return responseDto;
    }

    @PostMapping("/save")
    public ResponseDto<UserDto> save(@RequestBody UserDto userDto) {
        // 保存校验
        ValidatorUtil.require(userDto.getLoginName(), "登陆名");
        ValidatorUtil.length(userDto.getLoginName(), "登陆名", 1, 50);
        ValidatorUtil.length(userDto.getName(), "昵称", 1, 50);
        ValidatorUtil.require(userDto.getPassword(), "密码");

        // MD5 加密
        userDto.setPassword(DigestUtils.md5DigestAsHex(userDto.getPassword().getBytes()));
        ResponseDto<UserDto> responseDto = new ResponseDto<>();
        userService.save(userDto);
        responseDto.setContent(userDto);
        return responseDto;
    }

    /**
     * 保存密码
     *
     * @param userDto userDto
     * @return 返回响应
     */
    @PostMapping("/save-password")
    public ResponseDto<UserDto> savePassword(@RequestBody UserDto userDto) {
        // MD5 加密
        userDto.setPassword(DigestUtils.md5DigestAsHex(userDto.getPassword().getBytes()));
        ResponseDto<UserDto> responseDto = new ResponseDto<>();
        userService.savePassword(userDto);
        responseDto.setContent(userDto);
        return responseDto;
    }

    @DeleteMapping("/delete/{id}")
    public ResponseDto delete(@PathVariable String id) {
        ResponseDto responseDto = new ResponseDto();
        userService.delete(id);
        return responseDto;
    }

    /**
     * 登陆
     *
     * @param userDto userDto
     * @return 返回响应
     */
    @PostMapping("/login")
    public ResponseDto<LoginUserDto> login(@RequestBody UserDto userDto, HttpServletRequest request) {
        LOG.info("用户登陆开始");
        // MD5 加密
        userDto.setPassword(DigestUtils.md5DigestAsHex(userDto.getPassword().getBytes()));
        ResponseDto<LoginUserDto> responseDto = new ResponseDto<>();

        // 根据验证码 token 去获取缓存中的验证码 判断和用户输入的验证码是否一致
        String imageCode = (String) request.getSession().getAttribute(userDto.getImageCodeToken());

        if (StringUtils.isEmpty(imageCode)) {
            responseDto.setSuccess(false);
            responseDto.setMessage("验证码已过期");
            LOG.info("用户登陆失败: 验证码已过期");
            return responseDto;
        }

        if (!imageCode.toLowerCase().equals(userDto.getImageCode().toLowerCase())) {
            responseDto.setSuccess(false);
            responseDto.setMessage("验证码不对");
            LOG.info("用户登录失败: 验证码不对");
            return responseDto;
        } else {
            // 验证码通过后 移除验证码
            request.getSession().removeAttribute(userDto.getImageCodeToken());
        }
        LoginUserDto loginUserDto = userService.login(userDto);
        request.getSession().setAttribute("loginUser", loginUserDto);
        responseDto.setContent(loginUserDto);
        return responseDto;
    }

    /**
     * 退出登陆
     */
    @GetMapping("/logout")
    public ResponseDto logout(HttpServletRequest request) {
        ResponseDto responseDto = new ResponseDto();
        // 从 session 域中移除 loginUser
        request.getSession().removeAttribute(Constants.LOGIN_USER);
        return responseDto;
    }
}