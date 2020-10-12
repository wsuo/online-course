package com.lsu.server.config;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.support.spring.PropertyPreFilters;
import com.lsu.server.util.UuidUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;

/**
 * 切面类
 *
 * @Author wang suo
 * @Date 2020/10/12 0012 9:06
 * @Version 1.0
 */
@Aspect
@Component
public class LogAspect {

    private static final Logger LOG = LoggerFactory.getLogger(LogAspect.class);
    private static final String LIST = "list";
    private static final String QUERY = "query";
    private static final String GET = "get";
    private static final String SAVE = "save";
    private static final String DELETE = "delete";


    @Pointcut("execution(public * com.lsu.*.controller..*Controller.*(..))")
    public void pointcut() {
    }

    @Before("pointcut()")
    public void doBefore(JoinPoint joinPoint) {
        // 日志编号: 用来标识同一请求的日志
        MDC.put("UUID", UuidUtil.getShortUuid());

        // 开始打印请求日志
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes != null ? attributes.getRequest() : null;
        Signature signature = joinPoint.getSignature();
        String name = signature.getName();

        // 打印业务操作
        String nameCn = "";
        if (name.contains(LIST) || name.contains(QUERY) || name.contains(GET)) {
            nameCn = "查询";
        } else if (name.contains(SAVE)) {
            nameCn = "保存";
        } else if (name.contains(DELETE)) {
            nameCn = "删除";
        } else {
            nameCn = "操作";
        }

        // 使用反射: 获取业务名称
        Class clazz = signature.getDeclaringType();
        Field field;
        String businessName = "";
        try {
            field = clazz.getField("BUSINESS_NAME");
            if (!StringUtils.isEmpty(field)) {
                businessName = (String) field.get(clazz);
            }
        } catch (NoSuchFieldException e) {
            LOG.error("未获取到业务名称");
        } catch (IllegalAccessException e) {
            LOG.error("获取业务名称失败");
        }

        // 打印请求信息
        LOG.info("--------------------【{}】{}开始----------------------", businessName, nameCn);
        LOG.info("请求地址: {} {}", request != null ? request.getRequestURI().toString() : null,
                request != null ? request.getMethod() : null);
        LOG.info("类名方法: {}.{}", signature.getDeclaringTypeName(), name);
        LOG.info("远程地址: {}", request != null ? request.getRemoteAddr() : null);

        // 打印请求参数
        Object[] args = joinPoint.getArgs();
        Object[] arguments = new Object[args.length];
        for (int i = args.length - 1; i >= 0; i--) {
            if (args[i] instanceof ServletResponse || args[i] instanceof ServletRequest || args[i] instanceof MultipartFile) {
                continue;
            }
            arguments[i] = args[i];
        }
        // 排除字段: 敏感字段或者太长的字段不显示
        String[] excludeProperties = {};
        PropertyPreFilters filters = new PropertyPreFilters();
        PropertyPreFilters.MySimplePropertyPreFilter excludeFilter = filters.addFilter();
        excludeFilter.addExcludes(excludeProperties);
        LOG.info("请求参数: {}", JSONObject.toJSONString(arguments, excludeFilter));
    }

    @Around("pointcut()")
    public Object doAround(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object result = proceedingJoinPoint.proceed();
        // 排除字段
        String[] excludeProperties = {"password"};
        PropertyPreFilters filters = new PropertyPreFilters();
        PropertyPreFilters.MySimplePropertyPreFilter excludeFilter = filters.addFilter();
        excludeFilter.addExcludes(excludeProperties);
        LOG.info("返回结果: {}", JSONObject.toJSONString(result, excludeFilter));
        LOG.info("------------------ 结束 耗时 : {} ms --------------------", System.currentTimeMillis() - startTime);
        return result;
    }
}
