package com.daangndaangn.common.configure;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Mongodb 설정 클래스
 */
@Setter
@Getter
@Slf4j
@Component
@ConfigurationProperties(prefix = "spring.data.mongodb")
public class MongoConfigure {

    private String userName;

    private String password;

    private String host;

    private int port;

    private String database;
}
