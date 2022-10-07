package com.security.springsecurityjwt.config;

import io.jsonwebtoken.lang.Maps;
import lombok.var;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Description: 登录过滤器
 * @Created 2022/10/7 14:44
 **/
@Component
public class LoginFilter extends OncePerRequestFilter {

    @Resource
    private AuthenticationManager authenticationManager;

    @Resource
    private JwtHelper jwtHelper;

    /**
     * 校验账号密码是否正确
     * <p>
     * 1.AuthenticationManager.authenticate(UsernamePasswordAuthenticationToken token) token -- > UsernamePasswordAuthenticationToken extends AbstractAuthenticationToken implements Authentication
     * <p>
     * ProviderManager implements AuthenticationManager: 交给  ProviderManager 实现
     * <p>
     * 2.ProviderManager.authenticate(Authentication authentication)
     * <p>
     * 3.AuthenticationProvider.authenticate(authentication)
     * 内置代码 使用  AuthenticationProvider.authenticate(authentication); 去实现认证的核心业务
     * <p>
     * 4.AbstractUserDetailsAuthenticationProvider implements AuthenticationProvider : AuthenticationProvider.authenticate(authentication)
     * 交给 AbstractUserDetailsAuthenticationProvider 实现认证的核心业务
     * <p>
     * 5.DaoAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider : AbstractUserDetailsAuthenticationProvider.authenticate(authentication)
     * 交给 DaoAuthenticationProvider.authenticate(authentication) 实现认证核心业务
     *
     * @param request     请求
     * @param response    响应
     * @param filterChain 过滤器链
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        var username = request.getHeader("username");
        var password = request.getHeader("password");

        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

        response.setHeader(HttpHeaders.AUTHORIZATION, createJwtToken(authenticate));
    }

    /**
     * 创建Token
     *
     * @param authenticate
     * @return
     */
    private String createJwtToken(Authentication authenticate) {

        var user = (User) authenticate.getPrincipal();

        var roleString = user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(","));

        return jwtHelper.createToken(user.getUsername(), new HashMap<String, Object>() {{put("roles", roleString);}});

    }


    /**
     * 只针对登录的请求执行此过滤链
     *
     * @param request request
     * @return 过滤状态
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {

        var method = request.getMethod();
        var uri = request.getRequestURI();

        var isLoginUrl = HttpMethod.POST.matches(method) && StringUtils.startsWith(uri, "/login");

        return !isLoginUrl;
    }
}
