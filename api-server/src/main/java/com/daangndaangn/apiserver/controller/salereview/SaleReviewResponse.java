package com.daangndaangn.apiserver.controller.salereview;

import com.daangndaangn.apiserver.entity.review.SaleReview;
import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Getter;

public class SaleReviewResponse {

    /**
     * TODO: API 명세에 따라 필드값 수정 필요
     */
    @Getter
    @Builder
    @JsonNaming(SnakeCaseStrategy.class)
    public static class GetResponse {
        private Long id;
        private Long sellerId;
        private Long buyerId;
        private String content;

        public static GetResponse from(SaleReview saleReview) {
            return GetResponse.builder()
                    .id(saleReview.getId())
                    .sellerId(saleReview.getSeller().getId())
                    .buyerId(saleReview.getBuyer().getId())
                    .content(saleReview.getContent())
                    .build();
        }
    }
}
