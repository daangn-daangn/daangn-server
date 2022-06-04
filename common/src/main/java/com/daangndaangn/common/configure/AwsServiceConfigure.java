package com.daangndaangn.common.configure;

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
public class AwsServiceConfigure {

    private final AwsConfigure awsConfigure;

    @Bean
    public S3Presigner s3Presigner() {
        AwsCredentialsProvider credentialsProvider =
            StaticCredentialsProvider.create(
                AwsBasicCredentials.create(awsConfigure.getAccessKey(), awsConfigure.getSecretKey())
            );

        return S3Presigner.builder()
                .region(Region.AP_NORTHEAST_2)
                .credentialsProvider(credentialsProvider)
                .build();
    }
}
