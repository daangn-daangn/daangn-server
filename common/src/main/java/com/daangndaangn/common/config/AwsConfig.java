package com.daangndaangn.common.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Setter
@Getter
@Component
@ConfigurationProperties(prefix = "cloud.aws.s3")
@PropertySource(value = {"classpath:/aws.properties"}, ignoreResourceNotFound = true)
public class AwsConfig {

    private String region;

    private String url;

    private String bucket;

    private String accessKey;

    private String secretKey;
}
