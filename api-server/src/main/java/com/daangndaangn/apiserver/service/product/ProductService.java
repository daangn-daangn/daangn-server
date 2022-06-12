package com.daangndaangn.apiserver.service.product;

import com.daangndaangn.common.api.entity.product.Product;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductService {

    Product create(Long sellerId, Long categoryId, String title, String name, Long price, String description, List<String> productImageUrls);

    Product getProduct(Long id);

    Product getProductWithProductImages(Long id);

    void updateToSoldOut(Long id, Long buyerId);

    void update(Long id, Long buyerId);

    void update(Long id, int productStateCode);

    void update(Long id, String title, String name, Long categoryId, Long price, String description);

    void delete(Long id);

    boolean isSeller(Long id, Long userId);

    void refresh(Long id); // 끌어올리기
}
