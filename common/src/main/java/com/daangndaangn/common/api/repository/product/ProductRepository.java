package com.daangndaangn.common.api.repository.product;

import com.daangndaangn.common.api.entity.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
