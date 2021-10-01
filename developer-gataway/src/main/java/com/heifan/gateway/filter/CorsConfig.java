package com.heifan.gateway.filter;


import cn.hutool.core.lang.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.cors.reactive.CorsUtils;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

/**
 * z201.coding@gamil.com
 */
@Configuration
public class CorsConfig {

    private static final Logger log = LoggerFactory.getLogger(CorsConfig.class);

    private static final String ALLOWED_HEADERS = "x-requested-with, Content-Type , Image-Id , Authorization,Set-Cookie, X-XSRF-TOKEN,Client-Business-Group-Source,Client-Business-Source,Client-Business-Activity-Source,Client-Env-Source,Client-Platform-Source,Client-Start-Time,Client-Version-Source";
    private static final String ALLOWED_METHODS = "PUT,POST,GET,DELETE,OPTIONS";
    private static final String ALLOWED_EXPOSE = "x-requested-with, Content-Type , Image-Id  , Authorization,Set-Cookie, X-XSRF-TOKEN,Client-Business-Group-Source,Client-Business-Source,Client-Business-Activity-Source,Client-Env-Source,Client-Platform-Source,Client-Start-Time,Client-Version-Source";
    private static final String MAX_AGE = "18000L";

    @Bean
    public WebFilter corsFilter() {
        return (ServerWebExchange ctx, WebFilterChain chain) -> {
            ServerHttpRequest request = ctx.getRequest();
            if (CorsUtils.isCorsRequest(request)) {
                ServerHttpResponse response = ctx.getResponse();
                HttpHeaders headers = response.getHeaders();
                headers.add("Access-Control-Allow-Origin", request.getHeaders().getOrigin());
                headers.add("Access-Control-Allow-Methods", ALLOWED_METHODS);
                headers.add("Access-Control-Max-Age", MAX_AGE);
                headers.add("Access-Control-Allow-Headers", ALLOWED_HEADERS);
                headers.add("Access-Control-Expose-Headers", ALLOWED_EXPOSE);
                headers.add("Access-Control-Allow-Credentials", "true");
                if (request.getMethod() == HttpMethod.OPTIONS) {
                    response.setStatusCode(HttpStatus.OK);
                    String method = request.getMethod().name();
                    String host = request.getURI().getHost();
                    String path = request.getURI().getPath();
                    String info = String.format("Host:{%s} Method:{%s} Path:{%s}",
                            host,
                            method,
                            path
                    );
                    log.info("{}", info);
                    return Mono.empty();
                }
            }
            String path = ctx.getRequest().getURI().getPath();
            if (!Validator.isEmpty(path)) {
                if (path.contains(".ico")) {
                    ServerHttpResponse response = ctx.getResponse();
                    response.setStatusCode(HttpStatus.OK);
                    return Mono.empty();
                }
            }
            return chain.filter(ctx);
        };
    }



}
