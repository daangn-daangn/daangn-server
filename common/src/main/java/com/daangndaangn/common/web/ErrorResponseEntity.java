package com.daangndaangn.common.web;

import com.daangndaangn.common.error.DuplicateValueException;
import com.daangndaangn.common.error.NotFoundException;
import com.daangndaangn.common.error.UnauthorizedException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;

import static com.daangndaangn.common.web.ApiResult.ERROR;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

/**
 *
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ErrorResponseEntity {

    public static ResponseEntity<ApiResult<?>> from(Throwable throwable, boolean logFlag) {
        if (throwable instanceof IllegalArgumentException) {
            return new ResponseEntity<>(ERROR(throwable, BAD_REQUEST), BAD_REQUEST);
        } else if (throwable instanceof IllegalStateException) {
            return new ResponseEntity<>(ERROR(throwable, BAD_REQUEST), BAD_REQUEST);
        } else if (throwable instanceof DuplicateValueException) {
            return new ResponseEntity<>(ERROR(throwable, BAD_REQUEST), BAD_REQUEST);
        } else if (throwable instanceof UnauthorizedException) {
            return new ResponseEntity<>(ERROR(throwable, FORBIDDEN), FORBIDDEN);
        } else if (throwable instanceof NotFoundException) {
            return new ResponseEntity<>(ERROR(throwable, NOT_FOUND), NOT_FOUND);
        } else {
            if (logFlag) {
                log.warn("{}", throwable.getMessage(), throwable);
            }
            return new ResponseEntity<>(ERROR(throwable, INTERNAL_SERVER_ERROR), INTERNAL_SERVER_ERROR);
        }
    }
}
