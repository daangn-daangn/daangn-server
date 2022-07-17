package com.daangndaangn.notiserver.config;

import com.daangndaangn.common.security.CommonAccessDeniedHandler;
import com.daangndaangn.common.security.CommonUnauthorizedHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final CommonUnauthorizedHandler unauthorizedHandler;
    private final CommonAccessDeniedHandler accessDeniedHandler;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .csrf()
                .disable()
            .headers()
                .disable()
            .exceptionHandling()
                .accessDeniedHandler(accessDeniedHandler)
                .authenticationEntryPoint(unauthorizedHandler)
                .and()
            .formLogin()
                .disable()
            .authorizeRequests()
                .regexMatchers("^/actuator.*").permitAll() // 서버 상태 CHECK API는 모두 접근가능
            .anyRequest().authenticated();
    }
}
