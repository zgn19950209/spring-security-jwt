package com.security.springsecurityjwt.config;

import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.annotation.Resource;

/**
 * @Description:
 * @Created 2022/10/7 14:01
 **/
@EnableWebSecurity
@EnableMethodSecurity
public class SpringSecurityConfig {

    @Lazy
    @Resource
    private LoginFilter loginFilter;

    @Resource
    private BCryptPasswordEncoder bCryptPasswordEncoder;


    @Bean
    public UserDetailsService userDetailsService() {

        var uds = new InMemoryUserDetailsManager();

        uds.createUser(User.builder().username("user").password(bCryptPasswordEncoder.encode("user")).roles("USER").build());
        uds.createUser(User.builder().username("admin").password(bCryptPasswordEncoder.encode("admin")).roles("ADMIN").build());

        return uds;
    }


    /**
     * 定义认证管理器对象
     * <p>
     * DaoAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider implements AuthenticationProvider
     * ProviderManager implements AuthenticationManager
     * <p>
     * ProviderManager 依赖了  AuthenticationProvider ,故 认证管理器 最终交给 DaoAuthenticationProvider 去实现的
     *
     * @param userDetailsService 认证资源对象
     * @return 认证管理器对象
     */
    @Bean
    public AuthenticationManager authenticationManager(UserDetailsService userDetailsService) {

        var dao = new DaoAuthenticationProvider();
        dao.setUserDetailsService(userDetailsService);
        dao.setPasswordEncoder(bCryptPasswordEncoder);

        return new ProviderManager(dao);
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .csrf().disable()
                .authorizeRequests().anyRequest().authenticated()

                .and()
                .addFilterAt(loginFilter, BasicAuthenticationFilter.class);


        return http.build();
    }

}
