package com.daangndaangn.common.api.repository.product.query;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * Product, Product Seller 정보와 Product에 해당하는 favoriteCount를 나타내는 Dto
 */
@Getter
@AllArgsConstructor
public class ProductWithSellerQueryDto {
    private Long id;
    private String title;
    private String location;
    private Long price;
    private String imageUrl;
    private Long sellerId;
    private LocalDateTime createdAt;
    private Long favoriteCount;
}
