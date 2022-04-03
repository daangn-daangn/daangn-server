package com.daangndaangn.apiserver.controller.user;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;

import javax.validation.constraints.NotNull;

/**
 * 사용자 API 관련 요청
 */
public class UserRequest {

    @Getter
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class JoinRequest {
        @NotNull
        private String accessToken;
        @NotNull
        private String nickname;
        @NotNull
        private String location;
        private String profileUrl;
    }

}