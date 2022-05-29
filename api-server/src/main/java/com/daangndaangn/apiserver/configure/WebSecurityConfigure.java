package com.daangndaangn.apiserver.configure;

import com.daangndaangn.common.api.entity.user.Role;
import com.daangndaangn.apiserver.security.CustomAccessDeniedHandler;
import com.daangndaangn.apiserver.security.CustomUnauthorizedHandler;
import com.daangndaangn.apiserver.security.jwt.Jwt;
import com.daangndaangn.apiserver.security.jwt.JwtAuthenticationProvider;
import com.daangndaangn.apiserver.security.jwt.JwtAuthenticationTokenFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * SpringSecurity, 인증 인가 관련 설정 및 관련 Bean 모음
 */
@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class WebSecurityConfigure extends WebSecurityConfigurerAdapter {

    private final Jwt jwt;
    private final JwtConfigure jwtConfigure;
    private final JwtAuthenticationProvider jwtAuthenticationProvider;
    private final CustomUnauthorizedHandler unauthorizedHandler;
    private final CustomAccessDeniedHandler accessDeniedHandler;

    @Bean
    public JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter() {
        return new JwtAuthenticationTokenFilter(jwt, jwtConfigure.getHeader());
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // AuthenticationManager 는 AuthenticationProvider 목록을 지니고 있다.
        // 이 목록에 JwtAuthenticationProvider 를 추가한다.
        auth.authenticationProvider(jwtAuthenticationProvider);
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers("/swagger-resources", "/webjars/**", "/static/**", "/templates/**", "/h2/**");
    }

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
            .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                    .antMatchers("/api/hcheck").permitAll() // 서버 상태 CHECK API는 모두 접근가능
                    .antMatchers("/api/auth/login").permitAll()   // 로그인 API는 모두 접근가능
                    .antMatchers("/api/users/join").permitAll()  // 회원 가입 API는 모두 접근가능
                    .antMatchers("/api/**").permitAll()   // 그 외 API는 '회원 권한' 필요
//                    .antMatchers("/api/**").hasRole(Role.USER.name())   // 그 외 API는 '회원 권한' 필요
                .anyRequest().permitAll()
                .and()
            .formLogin()
                .disable();
        http
            .addFilterBefore(jwtAuthenticationTokenFilter(), UsernamePasswordAuthenticationFilter.class);
    }

}
