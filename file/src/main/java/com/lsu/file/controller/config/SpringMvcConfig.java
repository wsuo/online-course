package com.lsu.file.controller.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 文件上传配置
 *
 * @Author wang suo
 * @Date 2020/10/17 0017 14:30
 * @Version 1.0
 */
@Configuration
public class SpringMvcConfig implements WebMvcConfigurer {
    /**
     * SpringBoot 静态资源配置
     *
     * @param registry 注册类
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/f/**").addResourceLocations("file:D:/fileUpload/imooc/");
    }

    //http://127.0.0.1:9000/file/f/teacher/Nz1H76bO-头像2.jpg
}
