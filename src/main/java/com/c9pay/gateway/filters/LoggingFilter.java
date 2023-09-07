package com.c9pay.gateway.filters;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Component
@Slf4j
public class LoggingFilter extends AbstractGatewayFilterFactory<LoggingFilter.Config> {

    public LoggingFilter(){
        super(Config.class);
    }
    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            ServerHttpResponse response = exchange.getResponse();
            String uri = request.getURI().toString();
            String ip = Objects.requireNonNull(request.getRemoteAddress()).getAddress().toString();
            log.info("Request Service: {}", config.serviceName);
            log.info("Request Uri: {}", uri);
            log.info("Request Ip: {}", ip);
            return chain.filter(exchange).then(Mono.fromRunnable(() ->{
                String status  = Objects.requireNonNull(response.getStatusCode()).toString();
                log.info("Response Status: {}",status);
            }));
        };
    }

    @Data
    public static class Config{
        String serviceName;
    }
}
