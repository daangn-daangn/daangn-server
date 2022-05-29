package com.daangndaangn.common.api.repository.product.query;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * Product 정보와 Produoct에 해당하는 favoriteCount를 나타내는 Dto
 */
@Getter
@AllArgsConstructor
public class ProductQueryDto {
    private Long id;
    private String title;
    private String location;
    private Long price;
    private String imageUrl;
    private LocalDateTime createdAt;
    private Long favoriteCount;
}
