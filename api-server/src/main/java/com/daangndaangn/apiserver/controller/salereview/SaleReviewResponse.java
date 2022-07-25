package com.daangndaangn.apiserver.controller.salereview;

import com.daangndaangn.common.api.entity.review.SaleReview;
import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

public class SaleReviewResponse {

    @Getter
    @Builder
    @JsonNaming(SnakeCaseStrategy.class)
    public static class GetResponse {
        private Long id;
        private Long reviewerId;
        private String reviewer;
        private String profileUrl;
        private String location;
        private String content;
        private int reviewType;
        private LocalDateTime createdAt;

        public static GetResponse of(SaleReview saleReview, String profileUrl) {
            return GetResponse.builder()
                    .id(saleReview.getId())
                    .reviewerId(saleReview.getReviewer().getId())
                    .reviewer(saleReview.getReviewer().getNickname())
                    .profileUrl(profileUrl)
                    .location(saleReview.getReviewer().getLocation().getAddress())
                    .content(saleReview.getContent())
                    .reviewType(saleReview.getSaleReviewType().getCode())
                    .createdAt(saleReview.getCreatedAt())
                    .build();
        }
    }

    @Getter
    @AllArgsConstructor
    @JsonNaming(SnakeCaseStrategy.class)
    public static class CreateResponse {
        private Long saleReviewId;

        public static CreateResponse from(Long saleReviewId) {
            return new CreateResponse(saleReviewId);
        }
    }
}
