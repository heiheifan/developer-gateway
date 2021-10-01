package com.heifan.gateway.authentication;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

/**
 * @author z201.coding@gmail.com
 * @date 2020-09-01
 **/
@Getter
@Setter
@Builder
@AllArgsConstructor
public class JwtAuthenticationInfo {

    /**
     * 平台账号
     */
    Long accountId;

    /**
     * 系统用户信息
     */
    Long userId;

    /**
     * 登录系统
     */
    String loginSystem;

    /**
     * 用户允许访问系统
     */
    Set<String> systemSet;

    /**
     * 启用唯一在线
     */
    Boolean enableOnlyOnline;

    /**
     * 用户续期时长（时间戳）
     */
    Long renewalTimestamp;

    /**
     * 校验时间间隔（时间戳）
     */
    Long checkInterval;

    /**
     * jwt 过期时间
     */
    Long expiration;

    /**
     * 访问身份类型  AuthUserAccessIdentityEnum
     */
    Integer authUserAccessIdentityCode;

    private JwtAuthenticationInfo(){

    }

}
