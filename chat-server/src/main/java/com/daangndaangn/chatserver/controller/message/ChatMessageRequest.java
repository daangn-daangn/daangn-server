package com.daangndaangn.chatserver.controller.message;

import com.daangndaangn.common.chat.document.message.MessageType;
import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class ChatMessageRequest {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @JsonNaming(SnakeCaseStrategy.class)
    public static class CreateRequest {
        @NotEmpty
        private String roomId;
        @NotNull
        private Long senderId;

        private Long receiverId;

        @NotNull
        private Integer messageType;
        @NotEmpty
        private String message;

        public static CreateRequest of(String roomId, Long senderId) {
            return CreateRequest.builder()
                    .roomId(roomId)
                    .senderId(senderId)
                    .receiverId(null)
                    .messageType(MessageType.EXIT.getCode())
                    .message(MessageType.EXIT.getState())
                    .build();
        }
    }

    @Getter
    @JsonNaming(SnakeCaseStrategy.class)
    public static class UpdateRequest {
        @NotBlank(message = "roomId 값은 필수입니다.")
        private String roomId;
    }

    @Getter
    @JsonNaming(SnakeCaseStrategy.class)
    public static class ExitRequest {
        @NotBlank(message = "roomId 값은 필수입니다.")
        private String roomId;
    }
}
