package com.daangndaangn.chatserver.controller.message;

import com.daangndaangn.common.chat.document.message.ChatMessage;
import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

public class ChatMessageResponse {

    @Getter
    @Builder
    @JsonNaming(SnakeCaseStrategy.class)
    public static class GetResponse {
        private Long senderId;
        private String message;
        private LocalDateTime createdAt;

        public static GetResponse from(ChatMessage chatMessage) {
            return GetResponse.builder()
                    .senderId(chatMessage.getSenderId())
                    .message(chatMessage.getMessage())
                    .createdAt(chatMessage.getCreatedAt())
                    .build();
        }
    }
}
