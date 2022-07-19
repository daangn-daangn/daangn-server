package com.daangndaangn.apiserver.service.favorite;

import com.daangndaangn.common.api.entity.favorite.FavoriteProduct;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface FavoriteProductService {

    CompletableFuture<Long> create(Long productId, Long userId);

    FavoriteProduct getFavoriteProduct(Long id);

    List<FavoriteProduct> getFavoriteProductsByUser(Long userId, Pageable pageable);

    List<FavoriteProduct> getFavoriteProductsByProduct(Long productId);

    void delete(Long id);

    void delete(Long productId, Long userId);

    boolean isOwner(Long favoriteProductId, Long userId);
}
