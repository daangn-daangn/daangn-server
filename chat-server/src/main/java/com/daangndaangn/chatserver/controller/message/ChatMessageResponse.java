package com.daangndaangn.chatserver.controller.message;

import com.daangndaangn.common.chat.document.message.ChatMessage;
import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

public class ChatMessageResponse {

    @Getter
    @Builder
    @JsonNaming(SnakeCaseStrategy.class)
    public static class GetResponse {
        private Long senderId;
        private Integer messageType;
        private String message;
        private List<String> imgUrls;
        private LocalDateTime createdAt;

        /**
         * 일반 메시지 전용 응답(일반 메시지, 좌표)
         */
        public static GetResponse from(ChatMessage chatMessage) {
            return GetResponse.builder()
                    .senderId(chatMessage.getSenderId())
                    .messageType(chatMessage.getMessageType().getCode())
                    .message(chatMessage.getMessage()
                        .orElseThrow(
                            () -> new IllegalStateException(
                                String.format("%s 타입 message가 비어있습니다.", chatMessage.getMessageType().getState())
                            )
                        )
                    )
                    .createdAt(chatMessage.getCreatedAt())
                    .build();
        }

        /**
         * 사진 메시지 전용 응답
         */
        public static GetResponse of(ChatMessage chatMessage, List<String> presignedUrls) {
            return GetResponse.builder()
                    .senderId(chatMessage.getSenderId())
                    .messageType(chatMessage.getMessageType().getCode())
                    .imgUrls(presignedUrls)
                    .createdAt(chatMessage.getCreatedAt())
                    .build();
        }
    }

    /**
     * 이미지 업로드 요청 시 응답값 내려줌
     */
    @Getter
    @Builder
    @JsonNaming(SnakeCaseStrategy.class)
    public static class ImageUploadResponse {
        private String originName;
        private String imageName;
        private String presignedUrl;
    }
}
