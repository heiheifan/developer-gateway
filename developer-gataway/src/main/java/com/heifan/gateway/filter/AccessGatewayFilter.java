package com.heifan.gateway.filter;


import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.StrUtil;
import com.google.gson.Gson;
import com.heifan.gateway.authentication.JwtAuthenticationInfo;
import com.heifan.gateway.authentication.JwtAuthenticationManager;
import com.heifan.gateway.domian.GatewayResult;
import com.heifan.gateway.domian.PlatformHttpHeadersKey;
import org.bouncycastle.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 请求url权限校验
 *
 * @author z201.coding@gmail.com
 * http://dockone.io/article/1640913
 * https://spring.io/guides/gs/gateway/
 */
@Configuration
public class AccessGatewayFilter implements GlobalFilter {

    private static final Logger log = LoggerFactory.getLogger(AccessGatewayFilter.class);

    private static final String START_TIME = "startTime";


    public AccessGatewayFilter() {
        log.info("Loaded AccessGatewayFilter [Logging]");
    }

    /**
     * 生成日志随机数
     *
     * @return
     */
    private synchronized String currentTraceId() {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        UUID uuid = new UUID(random.nextInt(), random.nextInt());
        StringBuilder st = new StringBuilder(uuid.toString().replace("-", "").toLowerCase());
        st.append(Instant.now().toEpochMilli());
        int i = 0;
        while (i < 3) {
            i++;
            st.append(ThreadLocalRandom.current().nextInt(2));
        }
        return st.toString();
    }

    private static String toRaw(Flux<DataBuffer> body) {
        AtomicReference<String> rawRef = new AtomicReference<>();
        body.subscribe(buffer -> {
            byte[] bytes = new byte[buffer.readableByteCount()];
            buffer.read(bytes);
            DataBufferUtils.release(buffer);
            rawRef.set(Strings.fromUTF8ByteArray(bytes));
        });
        return rawRef.get();
    }

    /**
     * 1.首先网关检查token是否有效，无效直接返回401，不调用签权服务
     * 2.调用签权服务器看是否对该请求有权限，有权限进入下一个filter，没有权限返回401
     *
     * @param exchange
     * @param chain
     * @return
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        /**
         * 授权信息
         */
        String authentication = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        String clientBusinessGroupSource = request.getHeaders().getFirst(PlatformHttpHeadersKey.CLIENT_BUSINESS_GROUP_SOURCE);
        String clientBusinessSource = request.getHeaders().getFirst(PlatformHttpHeadersKey.CLIENT_BUSINESS_SOURCE);
        String clientBusinessActivitySource = request.getHeaders().getFirst(PlatformHttpHeadersKey.CLIENT_BUSINESS_ACTIVITY_SOURCE);
        String clientEnvSource = request.getHeaders().getFirst(PlatformHttpHeadersKey.CLIENT_EVN_SOURCE);
        String clientPlatformSource = request.getHeaders().getFirst(PlatformHttpHeadersKey.CLIENT_PLATFORM_SOURCE);
        String clientStartTime = request.getHeaders().getFirst(PlatformHttpHeadersKey.CLIENT_START_TIME);
        String clientVersionSource = request.getHeaders().getFirst(PlatformHttpHeadersKey.CLIENT_VERSION_SOURCE);
        String clientIp = request.getHeaders().getFirst(PlatformHttpHeadersKey.X_REAL_IP);
        if (StrUtil.isEmpty(clientBusinessGroupSource) ||
                StrUtil.isEmpty(clientBusinessSource) ||
                StrUtil.isEmpty(clientEnvSource) ||
                StrUtil.isEmpty(clientVersionSource)) {
//            log.warn("请求头参数不完整 ");
        }
        String url = request.getPath().value();
        String method = exchange.getRequest().getMethod().name();
        String host = exchange.getRequest().getURI().getHost();
        String path = exchange.getRequest().getURI().getPath();
        String accountId = StrUtil.EMPTY;
        if (null != clientIp) {
            exchange.getAttributes().put(PlatformHttpHeadersKey.X_REAL_IP, clientIp);
        }
        if (Validator.isNotEmpty(authentication)) {
            try {
                JwtAuthenticationInfo jwtAuthenticationInfo = JwtAuthenticationManager.jwtAuthenticationInfo(authentication);
                log.info("{}",new Gson().toJson(jwtAuthenticationInfo));
                if (Validator.isNotNull(jwtAuthenticationInfo) && Validator.isNotNull(jwtAuthenticationInfo.getAccountId())) {
                    accountId = String.valueOf(jwtAuthenticationInfo.getAccountId());
                }
            } catch (Exception e) {

            }
        }
        String info = String.format("{\"Host\":\"%s\",\"Ip\":\"%s\",\"Group\":\"%s\",\"Source\":\"%s\",\"Env\":\"%s\",\"Version\":\"%s\",\"Method\":\"%s\",\"Usr\":\"%s\",\"Path\":\"%s\"}",
                host,
                clientIp,
                clientBusinessGroupSource,
                clientBusinessSource,
                clientEnvSource,
                clientVersionSource,
                method,
                accountId,
                path
        );
        exchange.getAttributes().put(START_TIME, System.currentTimeMillis());
        return chain.filter(exchange).then(Mono.fromRunnable(() -> {
            Long startTime = exchange.getAttribute(START_TIME);
            if (startTime != null) {
                Long executeTime = (System.currentTimeMillis() - startTime);
                log.info("{}{}", executeTime + "ms",info);
            }
        }));
    }
    /**
     * 网关直接响应
     *
     * @param
     */
    private Mono<Void> buildResponse(ServerWebExchange serverWebExchange, GatewayResult result) {
        serverWebExchange.getResponse().setStatusCode(HttpStatus.OK);
        Gson gson = new Gson();
        DataBuffer buffer = serverWebExchange.getResponse()
                .bufferFactory().wrap(gson.toJson(result).getBytes());
        return serverWebExchange.getResponse().writeWith(Flux.just(buffer));
    }

}
