package com.daangndaangn.apiserver.repository.favorite;

import com.daangndaangn.apiserver.entity.favorite.FavoriteProduct;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface FavoriteProductCustomRepository {
    Optional<FavoriteProduct> findByProductAndUser(Long productId, Long userId);

    List<FavoriteProduct> findAll(Long userId, Pageable pageable);
}
