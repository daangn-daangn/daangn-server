package com.daangndaangn.apiserver.controller.salereview;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;

import javax.validation.constraints.NotEmpty;

public class SaleReviewRequest {

    @Getter
    @JsonNaming(SnakeCaseStrategy.class)
    public static class CreateRequest {
        @NotEmpty
        private Long sellerId;
        @NotEmpty
        private Long buyerId;
        @NotEmpty
        private String content;
    }

    @Getter
    @JsonNaming(SnakeCaseStrategy.class)
    public static class UpdateRequest {
        @NotEmpty
        private String content;
    }
}
