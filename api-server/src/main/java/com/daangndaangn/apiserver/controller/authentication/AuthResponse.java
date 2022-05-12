package com.daangndaangn.apiserver.controller.authentication;

import com.daangndaangn.common.api.entity.user.User;
import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Getter;
import org.apache.commons.lang3.ObjectUtils;

/**
 * 인증(로그인/로그아웃) 응답 Dto
 */
public class AuthResponse {

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

        public static AuthResponse.LoginResponse of(String apiToken, User user) {
            return AuthResponse.LoginResponse.builder()
                    .apiToken(apiToken)
                    .nickname(user.getNickname())
                    .location(ObjectUtils.isEmpty(user.getLocation()) ? null : user.getLocation().getAddress())
                    .profileUrl(user.getProfileUrl())
                    .manner(user.getManner())
                    .build();
        }
    }
}
