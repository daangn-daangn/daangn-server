package com.daangndaangn.apiserver.controller.salereview;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class SaleReviewRequest {

    @Getter
    @JsonNaming(SnakeCaseStrategy.class)
    public static class SellerReviewCreateRequest {
        @NotNull
        private Long productId;
        @NotNull
        private Long buyerId;
        @NotEmpty
        private String content;
    }

    @Getter
    @JsonNaming(SnakeCaseStrategy.class)
    public static class BuyerReviewCreateRequest {
        @NotNull
        private Long productId;
        @NotNull
        private Long sellerId;
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
