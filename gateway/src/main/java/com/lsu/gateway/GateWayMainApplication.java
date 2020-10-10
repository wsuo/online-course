package com.lsu.gateway;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.cors.reactive.CorsWebFilter;


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

    /*
     * 1.允许cookies跨域
     * 2.允许向该服务器提交请求的URI，*表示全部允许，在SpringMVC中，如果设成*，会自动转成当前请求头中的Origin
     * 3.允许访问的头信息,*表示全部
     * 4.预检请求的缓存时间（秒），即在这个时间段里，对于相同的跨域请求不会再预检了
     * 5.允许提交请求的方法，*表示全部允许
     *
     * @return 返回 reactive 包下的 CorsWebFilter 对象
     */
    /*@Bean
    public CorsWebFilter corsWebFilter() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        final CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("*");
        config.addAllowedHeader("*");
        config.setMaxAge(3600L);
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);
        return new CorsWebFilter(source);
    }*/
}
