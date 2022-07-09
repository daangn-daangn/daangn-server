package com.daangndaangn.apiserver.service.product.views;

import com.daangndaangn.apiserver.service.product.ProductService;
import com.daangndaangn.common.api.entity.product.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.ConcurrentHashMap;

@RequiredArgsConstructor
@Service
public class ProductViewService {

    private static final long UPDATE_VIEW_COUNT_INTERVAL = 5000L;

    private static final ConcurrentHashMap<Long, Integer> productViewingMap = new ConcurrentHashMap<>();
    private final ProductService productService;

    public void addProductViewCount(Long productId) {
        Integer value = productViewingMap.putIfAbsent(productId, 1);

        if (value != null) {
            productViewingMap.put(productId, ++value);
        }
    }

    @Scheduled(fixedDelay = UPDATE_VIEW_COUNT_INTERVAL)
    @Transactional
    public void updateProductViewCount() {
        productViewingMap.forEach((productId, viewCount) -> {
            Product product = productService.getProduct(productId);
            product.increaseViewCount(viewCount);
        });

        productViewingMap.clear();
    }
}
