package com.daangndaangn.apiserver.service.product.query;

import com.daangndaangn.common.api.entity.category.Category;
import com.daangndaangn.common.api.entity.product.Product;
import com.daangndaangn.common.api.entity.product.ProductImage;
import com.daangndaangn.common.api.entity.product.ProductState;
import com.daangndaangn.common.api.entity.user.Location;
import com.daangndaangn.common.api.entity.user.User;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Product 상세 정보 + favorite_count, chatting_count 내려주기 위해 별도의 Dto로 분리
 */
@Getter
@Builder
public class ProductDetailQueryDto {
    private Long id;
    private User seller;
    private User buyer;
    private Category category;
    private Long price;
    private String title;
    private String description;
    private Location location;
    private ProductState productState;
    private String thumbNailImage;
    private List<ProductImage> productImages;
    private Long favoriteCount;
    private Long chattingCount;
    private Boolean isFavorite;
    private LocalDateTime createdAt;

    public static ProductDetailQueryDto of(Product product, Long favoriteCount, Long chattingCount) {
        return ProductDetailQueryDto.builder()
                .id(product.getId())
                .seller(product.getSeller())
                .buyer(product.getBuyer())
                .category(product.getCategory())
                .price(product.getPrice())
                .title(product.getTitle())
                .description(product.getDescription())
                .location(product.getLocation())
                .productState(product.getProductState())
                .thumbNailImage(product.getThumbNailImage())
                .productImages(product.getProductImages())
                .favoriteCount(favoriteCount)
                .chattingCount(chattingCount)
                .isFavorite(false)
                .createdAt(product.getCreatedAt())
                .build();
    }

    public static ProductDetailQueryDto of(Product product, Long favoriteCount, Long chattingCount, boolean isFavorite) {
        return ProductDetailQueryDto.builder()
                .id(product.getId())
                .seller(product.getSeller())
                .buyer(product.getBuyer())
                .category(product.getCategory())
                .price(product.getPrice())
                .title(product.getTitle())
                .description(product.getDescription())
                .location(product.getLocation())
                .productState(product.getProductState())
                .thumbNailImage(product.getThumbNailImage())
                .productImages(product.getProductImages())
                .favoriteCount(favoriteCount)
                .chattingCount(chattingCount)
                .isFavorite(isFavorite)
                .createdAt(product.getCreatedAt())
                .build();
    }
}
