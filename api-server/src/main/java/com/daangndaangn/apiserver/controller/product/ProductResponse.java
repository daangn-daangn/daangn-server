package com.daangndaangn.apiserver.controller.product;

import com.daangndaangn.apiserver.service.product.query.ProductDetailQueryDto;
import com.daangndaangn.common.api.repository.product.query.ProductQueryDto;
import com.daangndaangn.common.api.repository.product.query.ProductWithSellerQueryDto;
import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
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
        private String thumbNailImage;
        private Long favoriteCount;
        private Long chattingCount;
        private LocalDateTime createdAt;

        public void updateImageUrl(String presigendImageUrl) {
            this.thumbNailImage = presigendImageUrl;
        }

        public static SimpleResponse of(ProductQueryDto productQueryDto, Long chattingCount) {
            return SimpleResponse.builder()
                    .id(productQueryDto.getId())
                    .title(productQueryDto.getTitle())
                    .location(productQueryDto.getLocation())
                    .price(productQueryDto.getPrice())
                    .thumbNailImage(productQueryDto.getImageUrl())
                    .favoriteCount(productQueryDto.getFavoriteCount())
                    .chattingCount(chattingCount)
                    .createdAt(productQueryDto.getCreatedAt())
                    .build();
        }
    }

    /**
     * SimpleResponse + sellerId field
     */
    @Getter
    @Builder
    @JsonNaming(SnakeCaseStrategy.class)
    public static class BuyerResponse {
        private Long id;
        private String title;
        private String location;
        private Long price;
        private String thumbNailImage;
        private Long sellerId;
        private Long favoriteCount;
        private Long chattingCount;
        private LocalDateTime createdAt;

        void updateImageUrl(String presigendImageUrl) {
            this.thumbNailImage = presigendImageUrl;
        }

        public static BuyerResponse of(ProductWithSellerQueryDto productQueryDto, Long chattingCount) {
            return BuyerResponse.builder()
                    .id(productQueryDto.getId())
                    .title(productQueryDto.getTitle())
                    .location(productQueryDto.getLocation())
                    .price(productQueryDto.getPrice())
                    .thumbNailImage(productQueryDto.getImageUrl())
                    .sellerId(productQueryDto.getSellerId())
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
        private String thumbNailImage;
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
                    .thumbNailImage(simpleResponse.getThumbNailImage())
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
                    .thumbNailImage(simpleResponse.getThumbNailImage())
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
        private Long sellerId;
        private String title;
        private String location;
        private Long price;
        private String thumbNailImage;
        private Long favoriteCount;
        private Long chattingCount;
        private boolean hasReview;
        private LocalDateTime createdAt;

        public static PurchaseHistoryResponse of(BuyerResponse response, boolean hasReview) {
            return PurchaseHistoryResponse.builder()
                    .id(response.getId())
                    .sellerId(response.getSellerId())
                    .title(response.getTitle())
                    .location(response.getLocation())
                    .price(response.getPrice())
                    .thumbNailImage(response.getThumbNailImage())
                    .favoriteCount(response.getFavoriteCount())
                    .chattingCount(response.getChattingCount())
                    .hasReview(hasReview)
                    .createdAt(response.getCreatedAt())
                    .build();
        }
    }

    @Getter
    @Builder
    @JsonNaming(SnakeCaseStrategy.class)
    public static class DetailResponse {
        private Long id;
        private UserDto seller;
        private UserDto buyer;
        private Long categoryId;
        private Long price;
        private String title;
        private String description;
        private String location;
        private String productState;
        private Integer viewCount;
        private String thumbNailImage;
        private List<String> productImages;
        private Long favoriteCount;
        private Long chattingCount;
        private Boolean isFavorite;
        private LocalDateTime createdAt;

        public static DetailResponse from(ProductDetailQueryDto productDetailQueryDto,
                                          List<String> presignedProductImages) {

            return DetailResponse.builder()
                    .id(productDetailQueryDto.getId())
                    .seller(UserDto.of(productDetailQueryDto.getSeller().getId(),
                                        productDetailQueryDto.getSeller().getNickname())
                    )
                    .buyer(isEmpty(productDetailQueryDto.getBuyer()) ? null
                            : UserDto.of(productDetailQueryDto.getBuyer().getId(),
                                        productDetailQueryDto.getBuyer().getNickname())
                    )
                    .categoryId(productDetailQueryDto.getCategory().getId())
                    .price(productDetailQueryDto.getPrice())
                    .title(productDetailQueryDto.getTitle())
                    .description(productDetailQueryDto.getDescription())
                    .location(productDetailQueryDto.getLocation().getAddress())
                    .productState(productDetailQueryDto.getProductState().getState())
                    .viewCount(productDetailQueryDto.getViewCount())
                    .thumbNailImage(isEmpty(presignedProductImages) ? null : presignedProductImages.get(0))
                    .productImages(presignedProductImages)
                    .favoriteCount(productDetailQueryDto.getFavoriteCount())
                    .chattingCount(productDetailQueryDto.getChattingCount())
                    .isFavorite(productDetailQueryDto.getIsFavorite())
                    .createdAt(productDetailQueryDto.getCreatedAt())
                    .build();
        }

        @Getter
        @AllArgsConstructor
        @JsonNaming(SnakeCaseStrategy.class)
        public static class UserDto {
            private Long id;
            private String name;

            public static UserDto of(Long id, String name) {
                return new UserDto(id, name);
            }
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
