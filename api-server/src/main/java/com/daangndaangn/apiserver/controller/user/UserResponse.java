package com.daangndaangn.apiserver.controller.user;

import com.daangndaangn.apiserver.entity.user.User;
import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Getter;

/**
 * 사용자 API 관련 응답
 */
public class UserResponse {

    @Getter
    @Builder
    @JsonNaming(SnakeCaseStrategy.class)
    public static class JoinResponse {

        private Long id;
        private Long oauthId;
        private String profileUrl;

        public static JoinResponse from(User user) {
            return JoinResponse.builder()
                    .id(user.getId())
                    .oauthId(user.getOauthId())
                    .profileUrl(user.getProfileUrl())
                    .build();
        }
    }

}
