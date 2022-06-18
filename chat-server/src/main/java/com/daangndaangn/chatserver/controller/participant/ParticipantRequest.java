package com.daangndaangn.chatserver.controller.participant;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;

import javax.validation.constraints.NotNull;

public class ParticipantRequest {
    @Getter
    @JsonNaming(SnakeCaseStrategy.class)
    public static class InviteRequest {
        @NotNull(message = "userId 값은 필수입니다.")
        private Long userId;
    }
}
