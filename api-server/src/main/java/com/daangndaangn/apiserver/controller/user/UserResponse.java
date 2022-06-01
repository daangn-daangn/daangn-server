package com.daangndaangn.apiserver.controller.user;

import com.daangndaangn.common.api.entity.user.User;
import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Getter;
import org.apache.commons.lang3.ObjectUtils;

import static org.apache.commons.lang3.ObjectUtils.isNotEmpty;

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

    @Getter
    @Builder
    @JsonNaming(SnakeCaseStrategy.class)
    public static class UserInfoResponse {
        private Long id;
        private Long oauthId;
        private String nickname;
        private String location;
        private String profileUrl;
        private double manner;

        public static UserInfoResponse from(User user) {
            return UserInfoResponse.builder()
                    .id(user.getId())
                    .oauthId(user.getOauthId())
                    .nickname(user.getNickname())
                    .location(isNotEmpty(user.getLocation()) ? user.getLocation().getAddress() : null)
                    .profileUrl(user.getProfileUrl())
                    .manner(user.getManner())
                    .build();
        }
    }
}

