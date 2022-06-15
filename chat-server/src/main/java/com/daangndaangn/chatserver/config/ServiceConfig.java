package com.daangndaangn.chatserver.config;

import com.daangndaangn.common.config.JwtConfig;
import com.daangndaangn.common.jwt.Jwt;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServiceConfig {

    @Bean
    public Jwt jwt(JwtConfig jwtConfig) {
        return new Jwt(jwtConfig.getIssuer(), jwtConfig.getClientSecret(), jwtConfig.getExpirySeconds());
    }
}
