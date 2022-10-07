package com.security.springsecurityjwt.config;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.Date;
import java.util.Map;

/**
 * @Description:
 * @Created 2022/10/7 18:38
 **/
@Component
public class JwtHelper {

    private final Key key;

    public JwtHelper(@Value("${jwt.secret}") String secret) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 创建token
     *
     * @param sub    主体信息
     * @param claims 声明
     * @return token信息
     */
    public String createToken(String sub, Map<String, Object> claims) {
        return Jwts
                .builder()
                .setSubject(sub)
                .addClaims(claims)
                .setExpiration(Date.from(Instant.now().plus(5, ChronoUnit.DAYS)))
                .signWith(key)
                .compact();
    }

    /**
     * 解析token
     *
     * @param token token
     * @return
     */
    public Map<String, Object> parseClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

}
