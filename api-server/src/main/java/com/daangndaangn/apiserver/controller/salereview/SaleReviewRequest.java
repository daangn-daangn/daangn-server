package com.daangndaangn.apiserver.controller.salereview;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class SaleReviewRequest {

    @Getter
    @JsonNaming(SnakeCaseStrategy.class)
    public static class SellerReviewCreateRequest {
        @NotNull(message = "productId 값은 필수입니다.")
        private Long productId;
        @NotNull(message = "buyerId 값은 필수입니다.")
        private Long buyerId;
        @NotBlank(message = "content 값은 필수입니다.")
        private String content;
    }

    @Getter
    @JsonNaming(SnakeCaseStrategy.class)
    public static class BuyerReviewCreateRequest {
        @NotNull(message = "productId 값은 필수입니다.")
        private Long productId;
        @NotNull(message = "sellerId 값은 필수입니다.")
        private Long sellerId;
        @NotBlank(message = "content 값은 필수입니다.")
        private String content;
    }

    @Getter
    @JsonNaming(SnakeCaseStrategy.class)
    public static class UpdateRequest {
        @NotBlank(message = "content 값은 필수입니다.")
        private String content;
    }
}
