package com.lsu.gateway.filter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;

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

    @Resource
    private RedisTemplate redisTemplate;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String admin = "/admin/";
        String login = "/system/admin/user/login";
        String logout = "/system/admin/user/logout";
        String kap = "/system/admin/kaptcha";
        String path = exchange.getRequest().getURI().getPath();

        ServerHttpResponse response = exchange.getResponse();

        // 请求地址中不包含 /admin 的 不是控制台请求: 不需要拦截
        if (!path.contains(admin)) {
            return chain.filter(exchange);
        }
        if (path.contains(login) || path.contains(logout) || path.contains(kap)) {
            LOG.info("不需要控台登录验证: {}", path);
            return chain.filter(exchange);
        }

        // 获取 header 的 token 参数
        String token = exchange.getRequest().getHeaders().getFirst("token");

        LOG.info("登录拦截开始, token: {}", token);
        if (StringUtils.isEmpty(token)) {
            LOG.info("token为空,请求被拦截");
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.setComplete();
        }

        Object obj = redisTemplate.opsForValue().get(token);
        if (obj == null) {
            LOG.info("token无效,请求被拦截");
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.setComplete();
        } else {
            LOG.info("已登录: {}", obj);

            // 增加权限校验: GateWay 中没有 LoginUserDto 所以全部使用 JSON 操作
            LOG.info("接口权限校验,请求地址: {}", path);
            boolean exist = false;
            JSONObject loginUserDto = JSON.parseObject(String.valueOf(obj));
            JSONArray requests = loginUserDto.getJSONArray("requests");

            // 遍历所有的权限请求: 判断当前请求的地址是否在权限请求中
            for (Object o : requests) {
                String request = (String) o;
                if (path.contains(request)) {
                    exist = true;
                    break;
                }
            }

            if (exist) {
                LOG.info("权限校验通过");
            } else {
                LOG.warn("权限校验未通过");
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }
            return chain.filter(exchange);
        }
    }

    @Override
    public int getOrder() {
        return 1;
    }
}
