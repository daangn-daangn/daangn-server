package com.daangndaangn.apiserver.service.product;

import com.daangndaangn.common.api.entity.product.Product;
import com.daangndaangn.common.api.entity.product.ProductState;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductService {

    void create(Long sellerId, Long categoryId, String title, String name, Long price, String description);

    List<Product> getProducts(Pageable pageable);

    Product getProduct(Long id);

    void update(Long id, Long buyerId);

    void update(Long id, ProductState productState);

    void update(Long id, String title, String name, Long categoryId, Long price, String description);

    void delete(Long id);
}
