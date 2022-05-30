package com.daangndaangn.apiserver.controller.favorite;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Getter;

public class FavoriteProductResponse {

    @Getter
    @AllArgsConstructor
    @JsonNaming(SnakeCaseStrategy.class)
    public static class CreateResponse {
        private Long favoriteId;

        public static CreateResponse from(Long favoriteId) {
            return new CreateResponse(favoriteId);
        }
    }
}
