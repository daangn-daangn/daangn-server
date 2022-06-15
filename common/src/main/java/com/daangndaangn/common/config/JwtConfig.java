package com.daangndaangn.common.config;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

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
