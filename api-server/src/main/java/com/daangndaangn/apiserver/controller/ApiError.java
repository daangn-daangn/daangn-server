package com.daangndaangn.apiserver.controller;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * API 예외 스펙
 * 모든 예외응답은 ApiError로 래핑되어 클라이언트에게 전달된다.
 */
@Getter
public class ApiError {

    private final String message;   // 오류 메시지

    private final int status;   // HTTP 오류코드

    public static ApiError of(Throwable throwable, HttpStatus status) {
        return new ApiError(throwable, status);
    }

    public static ApiError of(String message, HttpStatus status) {
        return new ApiError(message, status);
    }

    private ApiError(Throwable throwable, HttpStatus status) {
        this(throwable.getMessage(), status);
    }

    private ApiError(String message, HttpStatus status) {
        this.message = message;
        this.status = status.value();
    }
}
