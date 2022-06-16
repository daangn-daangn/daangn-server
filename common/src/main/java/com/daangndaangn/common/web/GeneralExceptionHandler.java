package com.daangndaangn.common.web;

import com.daangndaangn.common.error.DuplicateValueException;
import com.daangndaangn.common.error.NotFoundException;
import com.daangndaangn.common.error.ServiceRuntimeException;
import com.daangndaangn.common.error.UnauthorizedException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
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

    private ResponseEntity<ApiResult<?>> createResponseByThrowable(Throwable throwable, HttpStatus status) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<>(ERROR(throwable, status), headers, status);
    }

    private ResponseEntity<ApiResult<?>> createResponseByMessage(String errorMessage, HttpStatus status) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<>(ERROR(errorMessage, status), headers, status);
    }

    @ExceptionHandler({
            IllegalStateException.class, IllegalArgumentException.class,
            TypeMismatchException.class, HttpMessageNotReadableException.class,
            MissingServletRequestParameterException.class, MultipartException.class,
    })
    public ResponseEntity<?> handleBadRequestException(Exception e) {
        log.info("Bad request exception occurred: {}", e.getMessage(), e);
        return createResponseByThrowable(e, HttpStatus.BAD_REQUEST);
    }

    /**
     * Controller @Valid Exception 예외
     *
     * errorMessage 예시
     *
     * {
     *   "name" : "비어 있을 수 없습니다",
     *   "email" : "비어 있을 수 없습니다"
     * }
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidException(MethodArgumentNotValidException e) {
        log.info("Bad request exception occurred: {}", e.getMessage(), e);

        Map<String, String> errorMap = new HashMap<>();
        e.getBindingResult().getFieldErrors()
                .forEach(fieldError -> errorMap.put(fieldError.getField(), fieldError.getDefaultMessage()));

        String errorMessage = toErrorMessage(errorMap);
        return createResponseByMessage(errorMessage, HttpStatus.BAD_REQUEST);
    }

    private String toErrorMessage(Map<String, String> errorMap) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String errorMessage = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(errorMap);
            return errorMessage;
        } catch (JsonProcessingException ignored) {
            return "유효성 검사 예외가 발생했습니다.";
        }
    }

    @ExceptionHandler({
            AwsServiceException.class, SdkClientException.class
    })
    public ResponseEntity<?> handleAwsErrorException(Exception e) {
        log.info("AwsClientError exception occurred: {}", e.getMessage(), e);
        return createResponseByThrowable(e, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<?> handleHttpClientErrorException(HttpClientErrorException e) {
        log.info("HttpClientError exception occurred: {}", e.getMessage(), e);
        HttpStatus httpStatus = HttpStatus.valueOf(e.getRawStatusCode());
        return createResponseByThrowable(e, httpStatus);
    }

    @ExceptionHandler(RestClientException.class)
    public ResponseEntity<?> handleRestClientException(RestClientException e) {
        log.info("RestClient exception occurred: {}", e.getMessage(), e);
        return createResponseByThrowable(e, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * 정의한 비즈니스 예외가 발생하는 경우
     */
    @ExceptionHandler(ServiceRuntimeException.class)
    public ResponseEntity<?> handleServiceRuntimeException(ServiceRuntimeException e) {
        if (e instanceof NotFoundException) {
            return createResponseByThrowable(e, HttpStatus.NOT_FOUND);
        } else if (e instanceof UnauthorizedException) {
            return createResponseByThrowable(e, HttpStatus.FORBIDDEN);
        } else if (e instanceof DuplicateValueException) {
            return createResponseByThrowable(e, HttpStatus.BAD_REQUEST);
        }

        log.warn("Unexpected service exception occurred: {}", e.getMessage(), e);
        return createResponseByThrowable(e, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * 예상하지 못한 API Server Exception Handling
     */
    @ExceptionHandler({Exception.class, RuntimeException.class})
    public ResponseEntity<?> handleException(Exception e) {
        log.error("Unexpected exception occurred: {}", e.getMessage(), e);
        return createResponseByThrowable(e, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
