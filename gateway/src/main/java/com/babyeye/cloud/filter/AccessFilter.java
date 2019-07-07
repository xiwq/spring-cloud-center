package com.babyeye.cloud.filter;

import com.alibaba.fastjson.JSONObject;
import com.babyeye.cloud.constant.JsonConstant;
import com.babyeye.cloud.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author wenquan.
 * @date 2018/12/14.
 */
@Slf4j
@Configuration
public class AccessFilter implements GlobalFilter, Ordered {

    @Autowired
    private AuthService authService;

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String url = request.getPath().value();
        String token = request.getHeaders().getFirst("token");
        String result = authService.authentication(token, url);
        JSONObject jsonObject = JSONObject.parseObject(result);
        if (jsonObject.getIntValue(JsonConstant.RESULT_CODE) != 0) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            DataBuffer buffer = exchange.getResponse()
                    .bufferFactory().wrap(result.getBytes());
            return exchange.getResponse().writeWith(Flux.just(buffer));
        }
        return chain.filter(exchange);
    }
}
