package com.daangndaangn.apiserver.repository.favorite;

import com.daangndaangn.apiserver.entity.favorite.FavoriteProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoriteProductRepository extends JpaRepository<FavoriteProduct, Long>, FavoriteProductCustomRepository {
}
