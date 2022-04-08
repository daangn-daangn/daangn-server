package com.daangndaangn.apiserver.controller.authentication;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;

/**
 * 인증(로그인/로그아웃) 요청 Dto
 */
public class AuthRequest {

    @Getter
    @JsonNaming(SnakeCaseStrategy.class)
    public static class LoginRequest {
        private String accessToken;
    }

    @Getter
    @JsonNaming(SnakeCaseStrategy.class)
    public static class LogoutRequest {
        private String accessToken;
    }
}
