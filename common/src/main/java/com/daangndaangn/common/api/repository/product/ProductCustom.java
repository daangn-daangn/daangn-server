package com.daangndaangn.common.api.repository.product;

import com.daangndaangn.common.api.entity.product.Product;
import com.daangndaangn.common.api.entity.user.Location;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductCustom {
    List<Product> findAllProduct(String keyword, Long minPrice, Long maxPrice, Long categoryId, Location location, Pageable pageable);
}
