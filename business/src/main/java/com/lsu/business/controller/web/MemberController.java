package com.lsu.business.controller.web;

import com.alibaba.fastjson.JSON;
import com.lsu.server.dto.LoginMemberDto;
import com.lsu.server.dto.MemberDto;
import com.lsu.server.dto.ResponseDto;
import com.lsu.server.service.MemberService;
import com.lsu.server.util.UuidUtil;
import com.lsu.server.util.ValidatorUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.TimeUnit;

/**
 * 测试
 *
 * @Author wang suo
 * @Date 2020/10/9 0009 18:21
 * @Version 1.0
 */
@RestController("webMemberController")
@RequestMapping("/web/member")
public class MemberController {

    private static final Logger LOG = LoggerFactory.getLogger(MemberController.class);
    public static final String BUSINESS_NAME = "会员";

    @Resource
    private MemberService memberService;

    @Resource
    private RedisTemplate redisTemplate;

    @PostMapping("/register")
    public ResponseDto<MemberDto> register(@RequestBody MemberDto memberDto) {
        // 保存校验
        ValidatorUtil.require(memberDto.getMobile(), "手机号");
        ValidatorUtil.length(memberDto.getMobile(), "手机号", 11, 11);
        ValidatorUtil.require(memberDto.getPassword(), "密码");
        ValidatorUtil.length(memberDto.getName(), "昵称", 1, 50);
        ValidatorUtil.length(memberDto.getPhoto(), "头像URL", 1, 200);

        // 密码加密
        memberDto.setPassword(DigestUtils.md5DigestAsHex(memberDto.getPassword().getBytes()));

        ResponseDto<MemberDto> responseDto = new ResponseDto<>();
        memberService.save(memberDto);
        responseDto.setContent(memberDto);
        return responseDto;
    }

    /**
     * 登录
     *
     * @param memberDto memberDto
     * @return 返回响应
     */
    @PostMapping("/login")
    public ResponseDto<LoginMemberDto> login(@RequestBody MemberDto memberDto, HttpServletRequest request) {
        LOG.info("会员登陆开始");
        // MD5 加密
        memberDto.setPassword(DigestUtils.md5DigestAsHex(memberDto.getPassword().getBytes()));
        ResponseDto<LoginMemberDto> responseDto = new ResponseDto<>();

        // 根据验证码 token 去获取缓存中的验证码 判断和用户输入的验证码是否一致
        String imageCode = (String) redisTemplate.opsForValue().get(memberDto.getImageCodeToken());
        LOG.info("从Redis中获取到的验证码: {}", imageCode);

        if (StringUtils.isEmpty(imageCode)) {
            responseDto.setSuccess(false);
            responseDto.setMessage("验证码已过期");
            LOG.info("用户登陆失败: 验证码已过期");
            return responseDto;
        }

        if (!imageCode.toLowerCase().equals(memberDto.getImageCode().toLowerCase())) {
            responseDto.setSuccess(false);
            responseDto.setMessage("验证码不对");
            LOG.info("用户登录失败: 验证码不对");
            return responseDto;
        } else {
            // 验证码通过后 移除验证码
            redisTemplate.delete(memberDto.getImageCodeToken());
        }
        LoginMemberDto loginMemberDto = memberService.login(memberDto);
        String token = UuidUtil.getShortUuid();
        loginMemberDto.setToken(token);
        redisTemplate.opsForValue().set(token, JSON.toJSONString(loginMemberDto), 3600, TimeUnit.SECONDS);
        responseDto.setContent(loginMemberDto);
        return responseDto;
    }
}