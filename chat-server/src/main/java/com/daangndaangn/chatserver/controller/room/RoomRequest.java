package com.daangndaangn.chatserver.controller.room;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;

import java.util.List;

public class RoomRequest {

    @Getter
    @JsonNaming(SnakeCaseStrategy.class)
    public static class CreateRequest {
        private Long productId;
        private List<Long> userIds;
    }

}
