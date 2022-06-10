package com.daangndaangn.apiserver.security;

import com.daangndaangn.common.web.ApiResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.daangndaangn.common.web.ApiResult.ERROR;

/**
 * 인증 실패 시 401 응답을 내려주는 Handler
 */
@Component
@RequiredArgsConstructor
public class CustomUnauthorizedHandler implements AuthenticationEntryPoint {

    static ApiResult<?> E401 = ERROR("Authentication error (cause: unauthorized)", HttpStatus.UNAUTHORIZED);

    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setHeader("content-type", "application/json");
        response.getWriter().write(objectMapper.writeValueAsString(E401));
        response.getWriter().flush();
        response.getWriter().close();
    }
}
