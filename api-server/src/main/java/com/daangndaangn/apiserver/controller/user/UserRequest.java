package com.daangndaangn.apiserver.controller.user;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 사용자 API 관련 요청
 */
public class UserRequest {

    @Getter
    @JsonNaming(SnakeCaseStrategy.class)
    public static class JoinRequest {
        @NotBlank(message = "accessToken 값은 필수입니다.")
        private String accessToken;
    }

    @Getter
    @JsonNaming(SnakeCaseStrategy.class)
    public static class UpdateRequest {

        @NotBlank(message = "nickname 값은 필수입니다.")
        private String nickname;
        @NotBlank(message = "location 값은 필수입니다.")
        private String location;
        private String profileUrl;
    }

    @Getter
    @JsonNaming(SnakeCaseStrategy.class)
    public static class MannerRequest {
        @NotNull(message = "userId 값은 필수입니다.")
        private Long userId;

        @Min(value = -5)
        @Max(value = 5)
        @NotNull(message = "score 값은 필수입니다.")
        private Integer score;
    }
}
