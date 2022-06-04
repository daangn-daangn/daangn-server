package com.daangndaangn.apiserver.controller.product;

import com.daangndaangn.common.api.entity.product.Product;
import com.daangndaangn.common.api.repository.product.query.ProductQueryDto;
import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

import static org.apache.commons.lang3.ObjectUtils.isEmpty;

public class ProductResponse {

    @Getter
    @Builder
    @JsonNaming(SnakeCaseStrategy.class)
    public static class SimpleResponse {
        private Long id;
        private String title;
        private String location;
        private Long price;
        private String imageUrl;
        private Long favoriteCount;
        private Long chattingCount;
        private LocalDateTime createdAt;

        void updateImageUrl(String presigendImageUrl) {
            this.imageUrl = presigendImageUrl;
        }

        public static SimpleResponse of(ProductQueryDto productQueryDto, Long chattingCount) {
            return SimpleResponse.builder()
                    .id(productQueryDto.getId())
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
    @Builder
    @JsonNaming(SnakeCaseStrategy.class)
    public static class DetailResponse {
        private Long id;
        private String seller;
        private String buyer;
        private Long categoryId;
        private String name;
        private Long price;
        private String title;
        private String description;
        private String location;
        private String productState;
        private String thumbNailImage;
        private List<String> productImages;

        public static DetailResponse from(Product product, List<String> presignedProductImages) {
            return DetailResponse.builder()
                    .id(product.getId())
                    .seller(product.getSeller().getNickname())
                    .buyer(isEmpty(product.getBuyer()) ? null : product.getBuyer().getNickname())
                    .categoryId(product.getCategory().getId())
                    .name(product.getName())
                    .price(product.getPrice())
                    .title(product.getTitle())
                    .description(product.getDescription())
                    .location(product.getLocation().getAddress())
                    .productState(product.getProductState().getState())
                    .thumbNailImage(isEmpty(presignedProductImages) ? null : presignedProductImages.get(0))
                    .productImages(presignedProductImages)
                    .build();
        }
    }

    @Getter
    @Builder
    @JsonNaming(SnakeCaseStrategy.class)
    public static class CreateResponse {
        private Long productId;
        private List<String> productImages;

        public static CreateResponse from(Long productId, List<String> presignedProductImages) {
            return CreateResponse.builder()
                    .productId(productId)
                    .productImages(presignedProductImages)
                    .build();
        }
    }

}
