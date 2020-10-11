package com.lsu.gateway;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.core.env.ConfigurableEnvironment;


/**
 * 主启动类
 *
 * @Author wang suo
 * @Date 2020/10/9 0009 18:35
 * @Version 1.0
 */
@SpringBootApplication
@EnableDiscoveryClient
public class GateWayMainApplication {
    private static final Logger LOG = LoggerFactory.getLogger(GateWayMainApplication.class);

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(GateWayMainApplication.class);
        ConfigurableEnvironment env = app.run(args).getEnvironment();
        LOG.info(env.getProperty("spring.application.name") + "\t启动成功!" + "\t地址:\thttp://localhost:" + env.getProperty("server.port"));
    }
}
