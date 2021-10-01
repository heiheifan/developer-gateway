package com.heifan.gateway;

import com.heifan.config.alarm.EnableAlarmNoticeConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author z201.coding@gmail.com
 * @date 2020-09-08
 *
 *
 * #默认连接时间，默认45秒，太长了可以在此配置
 * spring.cloud.gateway.httpclient.connect-timeout=
 * #响应读取时间，默认不设置超时时间，可以在此设置
 * spring.cloud.gateway.httpclient.response-timeout=
 * #HttpClient连接池，最大空闲时间，默认无限制，可以在此配置
 * spring.cloud.gateway.httpclient.pool.max-idle-time=
 **/
@SpringBootApplication(scanBasePackages = "com.heifan.gateway")
public class GatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(com.heifan.gateway.GatewayApplication.class, args);
    }
}
