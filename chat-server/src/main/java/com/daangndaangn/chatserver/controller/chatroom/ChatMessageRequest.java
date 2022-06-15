package com.daangndaangn.chatserver.controller.chatroom;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;

public class ChatMessageRequest {

    @Getter
    @JsonNaming(SnakeCaseStrategy.class)
    public static class CreateRequest {
        private String roomId;
        private Long senderId;
        private Integer messageType;
        private String message;
    }
}
