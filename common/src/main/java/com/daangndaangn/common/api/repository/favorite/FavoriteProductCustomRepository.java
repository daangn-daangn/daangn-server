package com.daangndaangn.common.api.repository.favorite;

import com.daangndaangn.common.api.entity.favorite.FavoriteProduct;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface FavoriteProductCustomRepository {
    Optional<FavoriteProduct> findByProductAndUser(Long productId, Long userId);
    Optional<FavoriteProduct> findByIdWithUser(Long id);
    List<FavoriteProduct> findAll(Long userId, Pageable pageable);
    List<FavoriteProduct> findAll(Long productId);
    boolean exists(Long productId, Long userId);
}
