package com.lsu.gateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 登录控制台网关过滤器工厂
 *  给系统增加一个过滤器: 具体的实现是 LoginAdminGatewayFilter
 *
 * @Author wang suo
 * @Date 2020/10/23 0023 18:40
 * @Version 1.0
 */
@Component
public class LoginAdminGatewayFilterFactory extends AbstractGatewayFilterFactory<Object> {

    @Resource
    private LoginAdminGatewayFilter loginAdminGatewayFilter;

    @Override
    public GatewayFilter apply(Object config) {
        return loginAdminGatewayFilter;
    }
}
