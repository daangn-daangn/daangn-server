package com.daangndaangn.apiserver.service.favorite;

import com.daangndaangn.apiserver.entity.favorite.FavoriteProduct;
import com.daangndaangn.apiserver.entity.product.Product;
import com.daangndaangn.apiserver.entity.user.User;
import com.daangndaangn.apiserver.error.NotFoundException;
import com.daangndaangn.apiserver.repository.favorite.FavoriteProductRepository;
import com.daangndaangn.apiserver.service.product.ProductService;
import com.daangndaangn.apiserver.service.user.UserService;
import com.google.common.base.Preconditions;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class FavoriteProductServiceImpl implements FavoriteProductService {

    private final FavoriteProductRepository favoriteProductRepository;
    private final UserService userService;
    private final ProductService productService;

    @Transactional
    @Override
    public FavoriteProduct create(Long productId, Long userId) {
        Preconditions.checkArgument(productId != null, "productId 값은 필수입니다.");
        Preconditions.checkArgument(userId != null, "userId 값은 필수입니다.");

        FavoriteProduct favoriteProduct = favoriteProductRepository.findByProductAndUser(productId, userId)
                .orElseGet(() -> {

            User user = userService.findUser(userId);
            Product product = productService.findProduct(productId);
            return FavoriteProduct.builder()
                                    .user(user)
                                    .product(product)
                                    .build();
        });

        if (favoriteProduct.getId() == null) {
            return favoriteProductRepository.save(favoriteProduct);
        }

        favoriteProduct.update(true);
        return favoriteProduct;
    }

    @Override
    public FavoriteProduct findById(Long id) {
        Preconditions.checkArgument(id != null, "id 값은 필수입니다.");

        return favoriteProductRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(FavoriteProduct.class, String.format("favoriteProductId = %s", id)));
    }

    @Override
    public List<FavoriteProduct> findAll(Long userId, Pageable pageable) {
        Preconditions.checkArgument(userId != null, "userId 값은 필수입니다.");

        return favoriteProductRepository.findAll(userId, pageable);
    }

    @Transactional
    @Override
    public void delete(Long id) {
        Preconditions.checkArgument(id != null, "id 값은 필수입니다.");

        FavoriteProduct deletedFavoriteProduct = findById(id);
        deletedFavoriteProduct.update(false);
    }
}
