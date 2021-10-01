package com.heifan.gateway.domian;

/**
 * @author z201.coding@gmail.com
 **/
public interface PlatformHttpHeadersKey extends GatewayCacheKey{

    /**
     * 请求头
     */
    String HTTP_TOKEN_HEADER = "Authorization";
    /**
     * 请求头跟踪id名。
     */
    String HTTP_HEADER_TRACE_ID = "AppTraceId";

    /**
     * 业务组来源
     */
    String CLIENT_BUSINESS_GROUP_SOURCE = "Client-Business-Group-Source";

    /**
     * 完整长度 业务组来源 + 业务来源。
     */
    String CLIENT_BUSINESS_SOURCE = "Client-Business-Source";

    /**
     * 业务活动来源,主要针对特殊流量
     */
    String CLIENT_BUSINESS_ACTIVITY_SOURCE = "Client-Business-Activity-Source";

    /**
     * 平台环境
     * 1 ios
     * 2 android
     * 3 pc
     */
    String CLIENT_EVN_SOURCE = "Client-Env-Source";

    /**
     * 客户端平台信息 xxx手机 xxx版本浏览器
     */
    String CLIENT_PLATFORM_SOURCE = "Client-Platform-Source";

    /**
     * 客户端发起时间戳
     */
    String CLIENT_START_TIME = "Client-Start-Time";

    /**
     * 客户端版本来源 1.0.0
     */
    String CLIENT_VERSION_SOURCE = "Client-Version-Source";

    /**
     * 客户端来源
     */
    String X_REAL_IP = "x-real-ip";
}
