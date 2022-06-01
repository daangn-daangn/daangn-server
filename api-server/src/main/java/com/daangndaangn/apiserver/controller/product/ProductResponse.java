package com.daangndaangn.apiserver.controller.product;

import com.daangndaangn.common.api.repository.product.query.ProductQueryDto;
import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

public class ProductResponse {

    @Getter
    @Builder
    @JsonNaming(SnakeCaseStrategy.class)
    public static class SimpleResponse {
        private Long productId;
        private String title;
        private String location;
        private Long price;
        private String imageUrl;
        private Long favoriteCount;
        private Long chattingCount;
        private LocalDateTime createdAt;

        public static SimpleResponse of(ProductQueryDto productQueryDto, Long chattingCount) {
            return SimpleResponse.builder()
                    .productId(productQueryDto.getId())
                    .title(productQueryDto.getTitle())
                    .location(productQueryDto.getLocation())
                    .price(productQueryDto.getPrice())
                    .imageUrl(productQueryDto.getImageUrl())
                    .favoriteCount(productQueryDto.getFavoriteCount())
                    .chattingCount(chattingCount)
                    .createdAt(productQueryDto.getCreatedAt())
                    .build();
        }
    }

    @Getter
    @AllArgsConstructor
    @JsonNaming(SnakeCaseStrategy.class)
    public static class CreateResponse {
        private Long productId;

        public static CreateResponse from(Long productId) {
            return new CreateResponse(productId);
        }
    }

}
