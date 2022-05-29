package com.daangndaangn.apiserver.controller.product;

import com.daangndaangn.common.api.repository.product.query.ProductQueryDto;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

public class ProductResponse {

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    @Getter
    @Builder
    public static class SimpleResponse {
        private String title;
        private String location;
        private Long price;
        private String imageUrl;
        private Long favoriteCount;
        private Long chattingCount;
        private LocalDateTime createdAt;

        public static SimpleResponse of(ProductQueryDto productQueryDto, Long chattingCount) {
            return SimpleResponse.builder()
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
}
