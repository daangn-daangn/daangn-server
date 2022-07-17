package com.daangndaangn.chatserver.config;

import com.daangndaangn.common.api.entity.user.Role;
import com.daangndaangn.common.config.JwtConfig;
import com.daangndaangn.common.jwt.Jwt;
import com.daangndaangn.common.jwt.JwtAuthenticationTokenFilter;
import com.daangndaangn.common.security.CommonAccessDeniedHandler;
import com.daangndaangn.common.security.CommonUnauthorizedHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final Jwt jwt;
    private final JwtConfig jwtConfig;
    private final CommonUnauthorizedHandler unauthorizedHandler;
    private final CommonAccessDeniedHandler accessDeniedHandler;

    @Bean
    public JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter() {
        return new JwtAuthenticationTokenFilter(jwt, jwtConfig.getHeader());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .csrf()
                .disable()
            .headers()
                .disable()
            .cors()
                .configurationSource(configurationSource())
                .and()
            .exceptionHandling()
                .accessDeniedHandler(accessDeniedHandler)
                .authenticationEntryPoint(unauthorizedHandler)
                .and()
            .formLogin()
                .disable()
            .authorizeRequests()
                .regexMatchers("^/actuator.*").permitAll()
                .antMatchers("/chat/health").permitAll() // 서버 상태 CHECK API는 모두 접근가능
                .antMatchers("/chat/**").hasRole(Role.USER.name())   // 그 외 API는 '회원 권한' 필요
                .anyRequest().authenticated();

        http
            .addFilterBefore(jwtAuthenticationTokenFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    public CorsConfigurationSource configurationSource() {
        List<String> clientUrls = List.of("http://localhost:3001", "http://localhost:3002");

        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(clientUrls);
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
