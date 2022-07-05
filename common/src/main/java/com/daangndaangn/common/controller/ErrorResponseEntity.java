package com.daangndaangn.common.controller;

import com.daangndaangn.common.error.DuplicateValueException;
import com.daangndaangn.common.error.NotFoundException;
import com.daangndaangn.common.error.UnauthorizedException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;

import java.util.concurrent.CompletionException;

import static com.daangndaangn.common.controller.ApiResult.ERROR;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ErrorResponseEntity {

    public static ResponseEntity<ApiResult<?>> from(Throwable throwable, boolean logFlag) {
        if (throwable instanceof CompletionException) {
            if (isBadRequest(throwable.getMessage())) {
                return new ResponseEntity<>(ERROR(throwable, BAD_REQUEST), BAD_REQUEST);
            }

            if (isUnAuthorized(throwable.getMessage())) {
                return new ResponseEntity<>(ERROR(throwable, FORBIDDEN), FORBIDDEN);
            }

            if (isNotFound(throwable.getMessage())) {
                return new ResponseEntity<>(ERROR(throwable, NOT_FOUND), NOT_FOUND);
            }
        }

        if (logFlag) {
            log.warn("{}", throwable.getMessage(), throwable);
        }

        return new ResponseEntity<>(ERROR(throwable, INTERNAL_SERVER_ERROR), INTERNAL_SERVER_ERROR);
    }

    private static boolean isBadRequest(String message) {
        String illegalArgumentPrefix = IllegalArgumentException.class.getName();
        String illegalStatePrefix = IllegalStateException.class.getName();
        String duplicatePrefix = DuplicateValueException.class.getName();

        return message.startsWith(illegalArgumentPrefix)
                || message.startsWith(illegalStatePrefix)
                || message.startsWith(duplicatePrefix);
    }

    private static boolean isUnAuthorized(String message) {
        String unauthorizedPrefix = UnauthorizedException.class.getName();
        return message.startsWith(unauthorizedPrefix);
    }

    private static boolean isNotFound(String message) {
        String notFoundPrefix = NotFoundException.class.getName();
        return message.startsWith(notFoundPrefix);
    }
}
