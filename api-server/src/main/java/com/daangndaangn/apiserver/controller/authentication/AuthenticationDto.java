package com.daangndaangn.apiserver.controller.authentication;

import com.daangndaangn.apiserver.entity.user.User;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Getter;

/**
 * 인증(로그인) 요청/응답 Dto
 */
public class AuthenticationDto {

    @Getter
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class Request {
        private String accessToken;
    }

    @Builder
    @Getter
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class Response {
        //jwt
        private String apiToken;

        //userInfo
        private String nickname;
        private String email;
        private String location;
        private String profileUrl;
        private Double manner;

        public static Response of(String apiToken, User user) {
            return Response.builder()
                    .apiToken(apiToken)
                    .nickname(user.getNickname())
                    .email(user.getEmail().getAddress())
                    .location(user.getLocation().getAddress())
                    .profileUrl(user.getProfileUrl())
                    .manner(user.getManner())
                    .build();
        }
    }

}
