package com.daangndaangn.common.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class AwsServiceConfig {

    private final AwsConfig awsConfig;

    @Bean
    public S3Presigner s3Presigner() {
        AwsCredentialsProvider credentialsProvider =
            StaticCredentialsProvider.create(
                AwsBasicCredentials.create(awsConfig.getAccessKey(), awsConfig.getSecretKey())
            );

        return S3Presigner.builder()
                .region(Region.AP_NORTHEAST_2)
                .credentialsProvider(credentialsProvider)
                .build();
    }
}
