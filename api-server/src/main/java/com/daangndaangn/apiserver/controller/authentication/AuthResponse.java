package com.daangndaangn.apiserver.controller.authentication;

import com.daangndaangn.common.api.entity.user.User;
import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Getter;

/**
 * 인증(로그인/로그아웃) 응답 Dto
 */
public class AuthResponse {

    @Builder
    @Getter
    @JsonNaming(SnakeCaseStrategy.class)
    public static class AuthenticationResponse {
        //jwt
        private String apiToken;

        //userInfo
        private String nickname;
        private String location;
        private String profileUrl;
        private Double manner;

        public static AuthenticationResponse of(String apiToken, User user) {
            return AuthenticationResponse.builder()
                    .apiToken(apiToken)
                    .nickname(user.getNickname())
                    .location(user.getLocation() == null ? null : user.getLocation().getAddress())
                    .profileUrl(user.getProfileUrl())
                    .manner(user.getManner())
                    .build();
        }
    }

    /**
     * AuthenticationResponse에 있는 Original URL에 presigned-url을 적용하기 위한 클라스
     *
     * AuthenticationResponse + presigned-url 클래스
     */
    @Builder
    @Getter
    @JsonNaming(SnakeCaseStrategy.class)
    public static class LoginResponse {
        //jwt
        private String apiToken;

        //userInfo
        private String nickname;
        private String location;
        private String profileUrl;
        private Double manner;

        public static LoginResponse from(AuthenticationResponse response, String profileUrl) {
            return LoginResponse.builder()
                    .apiToken(response.getApiToken())
                    .nickname(response.getNickname())
                    .location(response.getLocation())
                    .profileUrl(profileUrl)
                    .manner(response.getManner())
                    .build();
        }
    }

}
