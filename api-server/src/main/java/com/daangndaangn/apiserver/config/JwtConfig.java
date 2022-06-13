package com.daangndaangn.apiserver.config;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Jwt 라이브러리 관련 application.yml 설정
 */
@ToString
@Setter
@Getter
@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtConfig {

    private String header;

    private String issuer;

    private String clientSecret;

    private int expirySeconds;
}
