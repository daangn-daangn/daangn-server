package com.daangndaangn.apiserver.controller.salereview;

import com.daangndaangn.common.api.entity.review.SaleReview;
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
        private String seller;
        private String buyer;
        private String content;

        public static GetResponse from(SaleReview saleReview) {
            return GetResponse.builder()
                    .id(saleReview.getId())
                    .seller(saleReview.getSeller().getNickname())
                    .buyer(saleReview.getBuyer().getNickname())
                    .content(saleReview.getContent())
                    .build();
        }
    }
}
