package com.security.springsecurityjwt.config;

import io.jsonwebtoken.Claims;
import lombok.var;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @Description:
 * @Created 2022/10/7 19:19
 **/
@Component
public class JwtFilter extends OncePerRequestFilter {

    @Resource
    private JwtHelper jwtHelper;


    /**
     * Jwt过滤链设计
     *
     * @param request
     * @param response
     * @param filterChain
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        var token = getToken(request);
        var claims = jwtHelper.parseClaims(token);

        // 创建认证信息对象 并赋值到springSecurity的上下文中
        var authentication = createAuthentication(claims);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }


    /**
     * 创建认证信息对象
     *
     * @param claims
     * @return
     */
    private Authentication createAuthentication(Map<String, Object> claims) {

        var roles = Arrays.stream(claims.get("roles").toString().split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        return new UsernamePasswordAuthenticationToken(claims.get(Claims.SUBJECT), null, roles);
    }


    /**
     * 获取 请求头上的token
     *
     * @param request
     * @return
     */
    private String getToken(HttpServletRequest request) {
        return Optional
                .ofNullable(request.getHeader(HttpHeaders.AUTHORIZATION))
                .filter(auth -> auth.startsWith("Bearer "))
                .map(auth -> auth.replace("Bearer ", ""))
                .orElseThrow(() -> new BadCredentialsException("Invalid Token"));
    }

    /**
     * 筛选除了login接口之外的接口
     *
     * @param request
     * @return
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {

        var method = request.getMethod();
        var uri = request.getRequestURI();

        var isLogin = HttpMethod.POST.matches(method) && StringUtils.startsWith(uri, "/login");
        return isLogin;
    }
}
