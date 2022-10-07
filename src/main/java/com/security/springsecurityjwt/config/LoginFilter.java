package com.security.springsecurityjwt.config;

import lombok.var;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Description: 登录过滤器
 * @Created 2022/10/7 14:44
 **/
@Component
public class LoginFilter extends OncePerRequestFilter {

    @Resource
    private AuthenticationManager authenticationManager;

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
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) {

        var username = request.getHeader("username");
        var password = request.getHeader("password");

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

        response.setHeader(HttpHeaders.AUTHORIZATION, username);

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
