package com.lsu.system;

import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.ConfigurableEnvironment;

/**
 * 主函数
 *
 * @Author wang suo
 * @Date 2020/10/9 0009 18:12
 * @Version 1.0
 */
@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan("com.lsu")
@MapperScan("com.lsu.server.mapper")
public class SystemMainApplication {
    private static final Logger LOG = LoggerFactory.getLogger(SystemMainApplication.class);

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(SystemMainApplication.class);
        ConfigurableEnvironment env = app.run(args).getEnvironment();
        LOG.info(env.getProperty("spring.application.name") + "\t启动成功!" + "\t地址:\thttp://localhost:" + env.getProperty("server.port"));
    }
}
