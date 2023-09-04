package com.c9pay.gateway.config;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpCookie;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

@Slf4j
@Component
public class UserCustomFilter extends AbstractGatewayFilterFactory<UserCustomFilter.Config> {


    public UserCustomFilter(){
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            ServerHttpResponse response = exchange.getResponse();
            MultiValueMap<String, HttpCookie> cookies = request.getCookies();
            for (List<HttpCookie> value : cookies.values()){
                for (HttpCookie httpCookie : value) {
                    log.info("cookie: {}", httpCookie.getValue());
                }
            }

            return chain.filter(exchange).then(Mono.fromRunnable(() ->{
                String status  = Objects.requireNonNull(response.getStatusCode()).toString();
                log.info("Response Status: {}",status);
            }));
        };
    }

    @Data
    public static class Config{

    }
}
