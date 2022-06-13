package com.daangndaangn.common.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Mongodb 설정 클래스
 */
@Setter
@Getter
@Component
@ConfigurationProperties(prefix = "spring.data.mongodb")
public class MongoConfig {

    private String database;

    private String uri;
}
