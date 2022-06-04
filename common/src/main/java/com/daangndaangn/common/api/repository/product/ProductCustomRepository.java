package com.daangndaangn.common.api.repository.product;

import com.daangndaangn.common.api.entity.product.Product;

import java.util.Optional;

public interface ProductCustomRepository {
    Optional<Product> findByProductId(Long productId);
    Optional<Product> findByProductIdWithProductImages(Long productId);
    Optional<Product> findByProductIdWithOnlySeller(Long productId);
}
