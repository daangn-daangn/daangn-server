package com.daangndaangn.chatserver.controller.participant;

import com.daangndaangn.common.chat.document.Participant;
import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

public class ParticipantResponse {

    @Builder
    @Getter
    @JsonNaming(SnakeCaseStrategy.class)
    public static class GetResponse {
        private String roomId;
        private String roomName;
        private String identifier;
        private String lastChat;
        private LocalDateTime createdAt;

        public GetResponse from(Participant participant) {
            return GetResponse.builder()
                    .roomId(participant.getChattingRoom().getId())
                    .lastChat("")
                    .createdAt(participant.getChattingRoom().getCreatedAt())
                    .build();
        }
    }
}
