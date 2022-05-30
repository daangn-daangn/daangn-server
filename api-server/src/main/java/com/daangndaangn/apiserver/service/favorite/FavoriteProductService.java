package com.daangndaangn.apiserver.service.favorite;

import com.daangndaangn.common.api.entity.favorite.FavoriteProduct;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface FavoriteProductService {

    Long create(Long productId, Long userId);

    FavoriteProduct getFavoriteProduct(Long id);

    List<FavoriteProduct> getFavoriteProducts(Long userId, Pageable pageable);

    void delete(Long id);
}
