package com.daangndaangn.apiserver.controller.chatroom;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;

import javax.validation.constraints.NotNull;

public class ChatRoomRequest {

    @Getter
    @JsonNaming(SnakeCaseStrategy.class)
    public static class CreateRequest {
        @NotNull
        private Long productId;
        @NotNull
        private Long otherUserId;
    }

}
