package com.daangndaangn.common.api.repository.favorite;

import com.daangndaangn.common.api.entity.favorite.FavoriteProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoriteProductRepository extends JpaRepository<FavoriteProduct, Long>, FavoriteProductCustomRepository {
}
