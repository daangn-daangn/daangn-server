package com.daangndaangn.apiserver.service.favorite;

import com.daangndaangn.common.api.entity.favorite.FavoriteProduct;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface FavoriteProductService {

    /**
     * DTO Region (for other controllers)
     */

    /**
     * Entity Region (for other services)
     */
    FavoriteProduct create(Long productId, Long userId);

    FavoriteProduct findById(Long id);

    List<FavoriteProduct> findAll(Long userId, Pageable pageable);

    void delete(Long id);
}
