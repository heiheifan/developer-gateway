package com.heifan.gateway.authentication;

import com.google.gson.Gson;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;

/**
 * @author z201.coding@gmail.com
 * @date 2020-09-01
 **/
@Slf4j
public class JwtAuthenticationManager {

    public static final String TOKEN_PREFIX = "Bearer ";

    /**
     * 签发的密钥
     */
    private static final String SECRET = "weliner.token";
    /**
     * 令牌用户信息
     */
    private static final String U_DATA = "U";

    public static JwtAuthenticationInfo jwtAuthenticationInfo(String token) {
        token = token.replace(TOKEN_PREFIX, "");
        Claims claims = null;
        try {
            claims = Jwts.parser()
                    .setSigningKey(SECRET)
                    .parseClaimsJws(token.trim())
                    .getBody();
        } catch (Exception e) {
            log.warn("token 获取失败 。");
            return null;
        }
        Gson gson = new Gson();
        Object obj = claims.get(U_DATA);
        if (null == obj) {
            return null;
        }
        return gson.fromJson(obj.toString(), JwtAuthenticationInfo.class);
    }

}
