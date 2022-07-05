package com.daangndaangn.common.security;

import com.daangndaangn.common.controller.ApiResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.daangndaangn.common.controller.ApiResult.ERROR;

/**
 * 인가 실패 시 403 응답을 내려주는 Handler
 */
@Component
@RequiredArgsConstructor
public class CommonAccessDeniedHandler implements AccessDeniedHandler {

    static ApiResult<?> E403 = ERROR("Authority error error (cause: forbidden)", HttpStatus.FORBIDDEN);

    private final ObjectMapper objectMapper;

    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException)
            throws IOException, ServletException {

        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setHeader("content-type", "application/json");
        response.getWriter().write(objectMapper.writeValueAsString(E403));
        response.getWriter().flush();
        response.getWriter().close();
    }
}
