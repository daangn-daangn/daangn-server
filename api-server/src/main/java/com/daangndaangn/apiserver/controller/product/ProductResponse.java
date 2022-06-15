package com.daangndaangn.apiserver.controller.product;

import com.daangndaangn.apiserver.service.product.query.ProductDetailQueryDto;
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

    /**
     * SimpleResponse + hasReview field
     */
    @Getter
    @Builder
    @JsonNaming(SnakeCaseStrategy.class)
    public static class SaleHistoryResponse {
        private Long id;
        private String title;
        private String location;
        private Long price;
        private String imageUrl;
        private Long favoriteCount;
        private Long chattingCount;
        private boolean hasReview;
        private LocalDateTime createdAt;

        public static SaleHistoryResponse of(SimpleResponse simpleResponse, boolean hasReview) {
            return SaleHistoryResponse.builder()
                    .id(simpleResponse.getId())
                    .title(simpleResponse.getTitle())
                    .location(simpleResponse.getLocation())
                    .price(simpleResponse.getPrice())
                    .imageUrl(simpleResponse.getImageUrl())
                    .favoriteCount(simpleResponse.getFavoriteCount())
                    .chattingCount(simpleResponse.getChattingCount())
                    .hasReview(hasReview)
                    .createdAt(simpleResponse.getCreatedAt())
                    .build();
        }

        public static SaleHistoryResponse from(SimpleResponse simpleResponse) {
            return SaleHistoryResponse.builder()
                    .id(simpleResponse.getId())
                    .title(simpleResponse.getTitle())
                    .location(simpleResponse.getLocation())
                    .price(simpleResponse.getPrice())
                    .imageUrl(simpleResponse.getImageUrl())
                    .favoriteCount(simpleResponse.getFavoriteCount())
                    .chattingCount(simpleResponse.getChattingCount())
                    .hasReview(false)
                    .createdAt(simpleResponse.getCreatedAt())
                    .build();
        }
    }

    /**
     * SimpleResponse + hasReview field
     */
    @Getter
    @Builder
    @JsonNaming(SnakeCaseStrategy.class)
    public static class PurchaseHistoryResponse {
        private Long id;
        private String title;
        private String location;
        private Long price;
        private String imageUrl;
        private Long favoriteCount;
        private Long chattingCount;
        private boolean hasReview;
        private LocalDateTime createdAt;

        public static PurchaseHistoryResponse of(SimpleResponse simpleResponse, boolean hasReview) {
            return PurchaseHistoryResponse.builder()
                    .id(simpleResponse.getId())
                    .title(simpleResponse.getTitle())
                    .location(simpleResponse.getLocation())
                    .price(simpleResponse.getPrice())
                    .imageUrl(simpleResponse.getImageUrl())
                    .favoriteCount(simpleResponse.getFavoriteCount())
                    .chattingCount(simpleResponse.getChattingCount())
                    .hasReview(hasReview)
                    .createdAt(simpleResponse.getCreatedAt())
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
        private Long favoriteCount;
        private Long chattingCount;
        private LocalDateTime createdAt;

        public static DetailResponse from(ProductDetailQueryDto productDetailQueryDto,
                                          List<String> presignedProductImages) {

            return DetailResponse.builder()
                    .id(productDetailQueryDto.getId())
                    .seller(productDetailQueryDto.getSeller().getNickname())
                    .buyer(isEmpty(productDetailQueryDto.getBuyer()) ? null : productDetailQueryDto.getBuyer().getNickname())
                    .categoryId(productDetailQueryDto.getCategory().getId())
                    .name(productDetailQueryDto.getName())
                    .price(productDetailQueryDto.getPrice())
                    .title(productDetailQueryDto.getTitle())
                    .description(productDetailQueryDto.getDescription())
                    .location(productDetailQueryDto.getLocation().getAddress())
                    .productState(productDetailQueryDto.getProductState().getState())
                    .thumbNailImage(isEmpty(presignedProductImages) ? null : presignedProductImages.get(0))
                    .productImages(presignedProductImages)
                    .favoriteCount(productDetailQueryDto.getFavoriteCount())
                    .chattingCount(productDetailQueryDto.getChattingCount())
                    .createdAt(productDetailQueryDto.getCreatedAt())
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
