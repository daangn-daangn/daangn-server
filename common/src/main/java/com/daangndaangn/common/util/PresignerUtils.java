package com.daangndaangn.common.util;

import com.daangndaangn.common.configure.AwsConfigure;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.time.Duration;

import static com.google.common.base.Preconditions.checkArgument;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;


@Component
@RequiredArgsConstructor
public class PresignerUtils {

    private static final String PRODUCT_IMAGE_FOLDER = "product-image";
    private final AwsConfigure awsConfigure;
    private final S3Presigner presigner;

    public String getProductPresignedGetUrl(String filename) {
        GetObjectPresignRequest presignGetRequest = toGetObjectPresignRequest(PRODUCT_IMAGE_FOLDER, filename);
        return presigner.presignGetObject(presignGetRequest).url().toString();
    }

    public String getProductPresignedPutUrl(String filename) {
        PutObjectPresignRequest presignPutRequest = toPutObjectPresignRequest(PRODUCT_IMAGE_FOLDER, filename);
        return presigner.presignPutObject(presignPutRequest).url().toString();
    }

    private GetObjectPresignRequest toGetObjectPresignRequest(String folder, String filename) {

        String filePathKey = toKey(folder, filename);

        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(awsConfigure.getBucket())
                .key(filePathKey)
                .build();

        return GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(10))
                .getObjectRequest(getObjectRequest)
                .build();
    }

    private PutObjectPresignRequest toPutObjectPresignRequest(String folder, String filename) {

        String filePathKey = toKey(folder, filename);

        PutObjectRequest objectRequest = PutObjectRequest.builder()
                .bucket(awsConfigure.getBucket())
                .key(filePathKey)
//                .acl(ObjectCannedACL.PUBLIC_READ)
                .build();

        return PutObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(10))
                .putObjectRequest(objectRequest)
                .build();
    }

    private String toKey(String folder, String filename) {
        checkArgument(isNotEmpty(folder), "folder name must not be empty");
        checkArgument(isNotEmpty(filename), "filename must not be empty");

        return folder + "/" + filename;
    }
}