package com.daangndaangn.apiserver.controller;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

/**
 * API 응답 스펙
 * 모든 응답(정상 예외)은 ApiResult로 래핑되어 클라이언트에게 전달된다.
 */
@ToString
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ApiResult<T> {

    private final boolean success;

    private final T response;

    private final ApiError error;

    public static <T> ApiResult<T> OK(T response) {
        return new ApiResult<>(true, response, null);
    }

    public static ApiResult<?> ERROR(Throwable throwable, HttpStatus status) {
        return new ApiResult<>(false, null, ApiError.of(throwable, status));
    }

    public static ApiResult<?> ERROR(String errorMessage, HttpStatus status) {
        return new ApiResult<>(false, null, ApiError.of(errorMessage, status));
    }
}
