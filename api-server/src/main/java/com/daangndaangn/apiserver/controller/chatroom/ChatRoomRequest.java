package com.daangndaangn.apiserver.controller.chatroom;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;

import javax.validation.constraints.NotNull;

public class ChatRoomRequest {

    @Getter
    @JsonNaming(SnakeCaseStrategy.class)
    public static class CreateRequest {
        @NotNull(message = "productId 값은 필수입니다.")
        private Long productId;
        @NotNull(message = "otherUserId 값은 필수입니다.")
        private Long otherUserId;
    }
}
