package com.lsu.gateway.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 登录控制台网关过滤器
 *
 * @Author wang suo
 * @Date 2020/10/23 0023 18:42
 * @Version 1.0
 */
@Component
public class LoginAdminGatewayFilter implements GatewayFilter, Ordered {

    private static final Logger LOG = LoggerFactory.getLogger(LoginAdminGatewayFilter.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 获取 header 的 token 参数
        String token = exchange.getRequest().getHeaders().getFirst("token");
        LOG.info("登录拦截开始, token: {}", token);
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 1;
    }
}
