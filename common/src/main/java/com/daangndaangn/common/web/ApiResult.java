package com.daangndaangn.common.web;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

import java.util.Map;

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

    public static ApiResult<?> ERROR(String errorMessage, HttpStatus status, Map<String, String> invalidFields) {
        return new ApiResult<>(false, null, ApiError.of(errorMessage, status, invalidFields));
    }
}
