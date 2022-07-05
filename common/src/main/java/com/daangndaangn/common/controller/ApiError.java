package com.daangndaangn.common.controller;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.Map;

@Getter
@JsonNaming(SnakeCaseStrategy.class)
public class ApiError {

    private final String message;   // 오류 메시지

    private final int status;   // HTTP 오류코드

    private final Map<String, String> invalidFields;    // 잘못된 데이터 요청으로 400 에러시, 유효하지 않은 필드 정보 return

    public static ApiError of(Throwable throwable, HttpStatus status) {
        return new ApiError(throwable, status);
    }

    public static ApiError of(String message, HttpStatus status) {
        return new ApiError(message, status);
    }

    public static ApiError of(String message, HttpStatus status, Map<String, String> invalidFields) {
        return new ApiError(message, status, invalidFields);
    }

    private ApiError(Throwable throwable, HttpStatus status) {
        this(throwable.getMessage(), status, null);
    }

    private ApiError(String message, HttpStatus status) {
        this(message, status, null);
    }

    private ApiError(String message, HttpStatus status, Map<String, String> invalidFields) {
        this.message = message;
        this.status = status.value();
        this.invalidFields = invalidFields;
    }
}
