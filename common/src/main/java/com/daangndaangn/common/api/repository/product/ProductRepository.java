package com.daangndaangn.common.api.repository.product;

import com.daangndaangn.common.api.entity.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long>, ProductCustom {

    Optional<Product> findById(Long productId);
}
