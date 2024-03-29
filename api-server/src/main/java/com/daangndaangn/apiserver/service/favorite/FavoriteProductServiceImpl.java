package com.daangndaangn.apiserver.service.favorite;

import com.daangndaangn.apiserver.service.product.ProductService;
import com.daangndaangn.common.api.entity.favorite.FavoriteProduct;
import com.daangndaangn.common.api.entity.product.Product;
import com.daangndaangn.common.api.entity.user.User;
import com.daangndaangn.common.api.repository.favorite.FavoriteProductRepository;
import com.daangndaangn.apiserver.service.user.UserService;
import com.daangndaangn.common.error.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.concurrent.CompletableFuture.completedFuture;
import static java.util.stream.Collectors.toList;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class FavoriteProductServiceImpl implements FavoriteProductService {

    private final FavoriteProductRepository favoriteProductRepository;
    private final UserService userService;
    private final ProductService productService;

    @Async
    @Override
    @Transactional
    public CompletableFuture<Long> create(Long productId, Long userId) {
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
            return completedFuture(favoriteProductRepository.save(favoriteProduct).getId());
        }

        favoriteProduct.update(true);
        return completedFuture(favoriteProduct.getId());
    }

    @Override
    public FavoriteProduct getFavoriteProduct(Long id) {
        checkArgument(id != null, "id 값은 필수입니다.");

        return favoriteProductRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(FavoriteProduct.class, String.format("favoriteProductId = %s", id)));
    }

    @Override
    public List<FavoriteProduct> getFavoriteProductsByUser(Long userId, Pageable pageable) {
        checkArgument(userId != null, "userId 값은 필수입니다.");
        return favoriteProductRepository.findAll(userId, pageable);
    }

    @Override
    public List<FavoriteProduct> getFavoriteProductsByProduct(Long productId) {
        checkArgument(productId != null, "productId 값은 필수입니다.");
        return favoriteProductRepository.findAll(productId);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        checkArgument(id != null, "id 값은 필수입니다.");

        FavoriteProduct deletedFavoriteProduct = getFavoriteProduct(id);
        deletedFavoriteProduct.update(false);
    }

    @Override
    @Transactional
    public void delete(Long productId, Long userId) {
        checkArgument(productId != null, "productId 값은 필수입니다.");
        checkArgument(userId != null, "userId 값은 필수입니다.");

        FavoriteProduct deletedFavoriteProduct = favoriteProductRepository.findByProductAndUser(productId, userId)
                .orElseThrow(() -> new NotFoundException(FavoriteProduct.class, String.format("productId = %s, userId = %s", productId, userId)));

        deletedFavoriteProduct.update(false);
    }

    @Override
    public boolean isOwner(Long favoriteProductId, Long userId) {
        checkArgument(favoriteProductId != null, "favoriteProductId 값은 필수입니다.");
        checkArgument(userId != null, "userId 값은 필수입니다.");

        FavoriteProduct favoriteProduct = favoriteProductRepository.findByIdWithUser(favoriteProductId)
            .orElseThrow(() -> new NotFoundException(FavoriteProduct.class, String.format("favoriteProductId = %s", favoriteProductId)));;

        return userId.equals(favoriteProduct.getUser().getId());
    }
}
