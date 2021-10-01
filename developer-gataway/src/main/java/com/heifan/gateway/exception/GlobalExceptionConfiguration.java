package com.heifan.gateway.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 网管统一异常处理
 *
 * @author z201.coding@gmail.com
 */
@Slf4j
@Order(-1)
@Component
@RequiredArgsConstructor
public class GlobalExceptionConfiguration implements ErrorWebExceptionHandler {

    private final ObjectMapper objectMapper;

    @Autowired
    private GateWayExceptionHandlerAdvice gateWayExceptionHandlerAdvice;

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable throwable) {
        ServerHttpResponse response = exchange.getResponse();
        if (response.isCommitted()) {
            return Mono.error(throwable);
        }
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        return response
                .writeWith(Mono.fromSupplier(() -> {
                    DataBufferFactory bufferFactory = response.bufferFactory();
                    try {
                        return bufferFactory.wrap(objectMapper.writeValueAsBytes(gateWayExceptionHandlerAdvice.handle(throwable)));
                    } catch (JsonProcessingException e) {
                        log.warn("Error writing response", throwable);
                        return bufferFactory.wrap(new byte[0]);
                    }
                }));
    }


}
