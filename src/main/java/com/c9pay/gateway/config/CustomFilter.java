package com.c9pay.gateway.config;

import com.c9pay.gateway.utils.UriParser;
import jdk.jfr.DataAmount;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Component
@Slf4j
public class CustomFilter extends AbstractGatewayFilterFactory<CustomFilter.Config> {

    public CustomFilter(){
        super(Config.class);
    }
        
    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            ServerHttpResponse response = exchange.getResponse();
            String uri = request.getURI().toString();
            log.info("external: {}", config.external);
            if(!UriParser.parse(uri, config.external)){
               return onError(exchange, "Invalid access", BAD_REQUEST);
            }
            return chain.filter(exchange);
        };
    }

    @Data
    public static class Config{
        String external;
    }

    private Mono<Void> onError(ServerWebExchange exchange, String message, HttpStatus status){
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(status);
        log.error(message);
        return response.setComplete();
    }

}
