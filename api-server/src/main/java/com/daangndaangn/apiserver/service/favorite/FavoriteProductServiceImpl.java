package com.daangndaangn.apiserver.service.favorite;

import com.daangndaangn.apiserver.error.NotFoundException;
import com.daangndaangn.apiserver.service.product.ProductService;
import com.daangndaangn.common.api.entity.favorite.FavoriteProduct;
import com.daangndaangn.common.api.entity.product.Product;
import com.daangndaangn.common.api.entity.user.User;
import com.daangndaangn.common.api.repository.favorite.FavoriteProductRepository;
import com.daangndaangn.apiserver.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;

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
        checkArgument(productId != null, "productId 값은 필수입니다.");
        checkArgument(userId != null, "userId 값은 필수입니다.");

        FavoriteProduct favoriteProduct = favoriteProductRepository.findByProductAndUser(productId, userId)
                .orElseGet(() -> {

            User user = userService.getUser(userId);
            Product product = productService.getProduct(productId);
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
        checkArgument(id != null, "id 값은 필수입니다.");

        return favoriteProductRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(FavoriteProduct.class, String.format("favoriteProductId = %s", id)));
    }

    @Override
    public List<FavoriteProduct> findAll(Long userId, Pageable pageable) {
        checkArgument(userId != null, "userId 값은 필수입니다.");
        return favoriteProductRepository.findAll(userId, pageable);
    }

    @Override
    public int getNumberOfFavorites(Long productId) {
        checkArgument(productId != null, "productId 값은 필수입니다.");
        return favoriteProductRepository.getNumberOfFavorites(productId);
    }

    @Transactional
    @Override
    public void delete(Long id) {
        checkArgument(id != null, "id 값은 필수입니다.");

        FavoriteProduct deletedFavoriteProduct = findById(id);
        deletedFavoriteProduct.update(false);
    }
}
