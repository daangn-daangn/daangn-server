package com.daangndaangn.apiserver.service.favorite;

import com.daangndaangn.common.api.entity.favorite.FavoriteProduct;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface FavoriteProductService {

    CompletableFuture<Long> create(Long productId, Long userId);

    FavoriteProduct getFavoriteProduct(Long id);

    List<FavoriteProduct> getFavoriteProducts(Long userId, Pageable pageable);

    void delete(Long id);

    boolean isOwner(Long favoriteProductId, Long userId);
}
