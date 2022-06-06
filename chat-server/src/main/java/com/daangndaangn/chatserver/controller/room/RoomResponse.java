package com.daangndaangn.chatserver.controller.room;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Getter;

public class RoomResponse {

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
