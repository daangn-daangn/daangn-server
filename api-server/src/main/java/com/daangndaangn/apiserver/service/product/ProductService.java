package com.daangndaangn.apiserver.service.product;

import com.daangndaangn.common.api.entity.product.Product;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductService {

    Long create(Long sellerId, Long categoryId, String title, String name, Long price, String description);

    //TODO
    List<Product> getProducts(Pageable pageable);

    Product getProduct(Long id);

    void update(Long id, Long buyerId);

    void update(Long id, int productStateCode);

    void update(Long id, String title, String name, Long categoryId, Long price, String description);

    void delete(Long id);
}
