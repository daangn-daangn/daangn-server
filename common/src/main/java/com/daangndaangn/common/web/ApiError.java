package com.daangndaangn.common.web;

import lombok.Getter;
import org.springframework.http.HttpStatus;

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
