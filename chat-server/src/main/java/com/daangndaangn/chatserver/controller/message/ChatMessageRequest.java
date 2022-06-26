package com.daangndaangn.chatserver.controller.message;

import com.daangndaangn.common.chat.document.message.MessageType;
import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

import javax.validation.constraints.*;
import java.util.List;

public class ChatMessageRequest {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @JsonNaming(SnakeCaseStrategy.class)
    public static class CreateRequest {
        @NotBlank(message = "roomId 값은 필수입니다.")
        private String roomId;
        @NotNull(message = "senderId 값은 필수입니다.")
        private Long senderId;

        private Long receiverId;

        @Min(value = 1, message = "messageType 값은 1~3 입니다.")
        @Max(value = 3 , message = "messageType 값은 1~3 입니다.")
        @NotNull(message = "messageType 값은 필수입니다.")
        private Integer messageType;
        @NotBlank(message = "message 내용은 필수입니다.")
        private String message;

        /**
         * '채팅방 나가기' 메시지 생성
         */
        public static CreateRequest of(String roomId, Long senderId) {
            return CreateRequest.builder()
                    .roomId(roomId)
                    .senderId(senderId)
                    .receiverId(null)
                    .messageType(MessageType.EXIT.getCode())
                    .message(MessageType.EXIT.getState())
                    .build();
        }

        /**
         * 이미지 타입 메시지 생성
         */
        public static CreateRequest of(CreateRequest request, String presignedUrl) {
            return CreateRequest.builder()
                    .roomId(request.getRoomId())
                    .senderId(request.getSenderId())
                    .receiverId(request.getReceiverId())
                    .messageType(MessageType.IMAGE.getCode())
                    .message(presignedUrl)
                    .build();
        }
    }

    @Getter
    @JsonNaming(SnakeCaseStrategy.class)
    public static class ImageUploadRequest {
        @Size(max = 10)
        private List<String> imgFiles;
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
