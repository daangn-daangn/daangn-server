package com.daangndaangn.apiserver.controller.chatroom;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Getter;

public class ChatRoomResponse {

    @Getter
    @AllArgsConstructor
    @JsonNaming(SnakeCaseStrategy.class)
    public static class CreateResponse {
        String roomId;

        public static CreateResponse from(String chattingRoomId) {
            return new CreateResponse(chattingRoomId);
        }
    }
}
