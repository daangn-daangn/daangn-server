package com.daangndaangn.apiserver.security.oauth;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * OAuthService에 사용자 정보를 요청하기 위해 필요한 parameter(ex. accessToken)
 */
public class OAuthRequest {

    @Getter
    @AllArgsConstructor
    public static class LoginRequest {
        private String accessToken;

        public static OAuthRequest.LoginRequest from(String accessToken) {
            return new OAuthRequest.LoginRequest(accessToken);
        }
    }

    @Getter
    @AllArgsConstructor
    public static class LogoutRequest {
        private String accessToken;

        public static OAuthRequest.LogoutRequest from(String accessToken) {
            return new OAuthRequest.LogoutRequest(accessToken);
        }
    }
}
