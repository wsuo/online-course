package com.lsu.business;

import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.ConfigurableEnvironment;

/**
 * 主启动类
 *
 * @Author wang suo
 * @Date 2020/10/10 0010 16:29
 * @Version 1.0
 */
@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan("com.lsu")
@MapperScan("com.lsu.server.mapper")
public class BusinessMainApplication {

    private static final Logger LOG = LoggerFactory.getLogger(BusinessMainApplication.class);

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(BusinessMainApplication.class);
        ConfigurableEnvironment env = app.run(args).getEnvironment();
        LOG.info(env.getProperty("spring.application.name") + "\t启动成功!" + "\t地址:\thttp://localhost:" + env.getProperty("server.port"));
    }
}
