package com.daangndaangn.apiserver.repository.product;

import com.daangndaangn.apiserver.entity.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long>, ProductCustom {

    Optional<Product> findById(Long productId);
}