package com.daangndaangn.common.web;

import com.daangndaangn.common.error.DuplicateValueException;
import com.daangndaangn.common.error.NotFoundException;
import com.daangndaangn.common.error.ServiceRuntimeException;
import com.daangndaangn.common.error.UnauthorizedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.multipart.MultipartException;
import software.amazon.awssdk.awscore.exception.AwsServiceException;
import software.amazon.awssdk.core.exception.SdkClientException;

import java.util.HashMap;
import java.util.Map;

import static com.daangndaangn.common.web.ApiResult.ERROR;

@Slf4j
@RestControllerAdvice
public class GeneralExceptionHandler {

    private ResponseEntity<ApiResult<?>> createResponse(Throwable throwable, HttpStatus status) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<>(ERROR(throwable, status), headers, status);
    }

    private ResponseEntity<ApiResult<?>> createResponseByInvalidFields(String errorMessage,
                                                                       HttpStatus status,
                                                                       Map<String, String> invalidFields) {

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<>(ERROR(errorMessage, status, invalidFields), headers, status);
    }

    /**
     * AuthenticationException 인증 과정중에 일어난 에러도 400으로 처리
     */
    @ExceptionHandler({
            IllegalStateException.class, IllegalArgumentException.class,
            TypeMismatchException.class, HttpMessageNotReadableException.class,
            MissingServletRequestParameterException.class, MultipartException.class,
            AuthenticationException.class
    })
    public ResponseEntity<?> handleBadRequestException(Exception e) {
        return createResponse(e, HttpStatus.BAD_REQUEST);
    }

    /**
     * ApiController 내 @Valid 전용 예외
     *
     * invalid_fields 예시
     *
     * {
     *   "name" : "비어 있을 수 없습니다",
     *   "email" : "비어 있을 수 없습니다"
     * }
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidException(MethodArgumentNotValidException e) {
        Map<String, String> errorMap = new HashMap<>();
        e.getBindingResult().getFieldErrors()
                .forEach(fieldError -> errorMap.put(fieldError.getField(), fieldError.getDefaultMessage()));

        String errorMessage = "Bad request exception occurred";
        return createResponseByInvalidFields(errorMessage, HttpStatus.BAD_REQUEST, errorMap);
    }

    @ExceptionHandler({
            AwsServiceException.class, SdkClientException.class
    })
    public ResponseEntity<?> handleAwsErrorException(Exception e) {
        return createResponse(e, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<?> handleHttpClientErrorException(HttpClientErrorException e) {
        HttpStatus httpStatus = HttpStatus.valueOf(e.getRawStatusCode());
        return createResponse(e, httpStatus);
    }

    @ExceptionHandler(RestClientException.class)
    public ResponseEntity<?> handleRestClientException(RestClientException e) {
        return createResponse(e, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * 정의한 비즈니스 예외가 발생하는 경우
     */
    @ExceptionHandler(ServiceRuntimeException.class)
    public ResponseEntity<?> handleServiceRuntimeException(ServiceRuntimeException e) {
        if (e instanceof NotFoundException) {
            return createResponse(e, HttpStatus.NOT_FOUND);
        } else if (e instanceof UnauthorizedException) {
            return createResponse(e, HttpStatus.FORBIDDEN);
        } else if (e instanceof DuplicateValueException) {
            return createResponse(e, HttpStatus.BAD_REQUEST);
        }

        log.warn("Unexpected service exception occurred: {}", e.getMessage(), e);
        return createResponse(e, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * 예상하지 못한 API Server Exception Handling
     */
    @ExceptionHandler({Exception.class, RuntimeException.class})
    public ResponseEntity<?> handleException(Exception e) {
        log.error("Unexpected exception occurred: {}", e.getMessage(), e);
        return createResponse(e, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
