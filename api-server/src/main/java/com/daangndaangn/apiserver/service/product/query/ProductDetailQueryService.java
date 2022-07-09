package com.daangndaangn.apiserver.service.product.query;

import com.daangndaangn.apiserver.service.product.ProductService;
import com.daangndaangn.common.api.entity.product.Product;
import com.daangndaangn.common.api.repository.favorite.FavoriteProductRepository;
import com.daangndaangn.common.api.repository.product.query.ProductQueryDto;
import com.daangndaangn.common.api.repository.product.query.ProductQueryRepository;
import com.daangndaangn.common.chat.repository.chatroom.ChatRoomRepository;
import com.daangndaangn.common.error.NotFoundException;
import com.daangndaangn.common.event.ProductViewEvent;
import com.google.common.eventbus.EventBus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class ProductDetailQueryService {

    private final ProductService productService;
    private final ProductQueryRepository productQueryRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final FavoriteProductRepository favoriteProductRepository;
    private final EventBus eventBus;

    public ProductDetailQueryDto getProductDetail(Long id) {

        ProductQueryDto productQueryDto = productQueryRepository.findById(id)
            .orElseThrow(() -> new NotFoundException(ProductQueryDto.class, String.format("productId = %s", id)));

        Product productWithProductImages = productService.getProductWithProductImages(id);
        long chattingCount = chatRoomRepository.countAllByProductId(id);

        return ProductDetailQueryDto.of(productWithProductImages, productQueryDto.getFavoriteCount(), chattingCount);
    }

    public ProductDetailQueryDto getProductDetail(Long id, Long userId) {

        ProductQueryDto productQueryDto = productQueryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ProductQueryDto.class, String.format("productId = %s", id)));

        Product productWithProductImages = productService.getProductWithProductImages(id);
        long chattingCount = chatRoomRepository.countAllByProductId(id);
        boolean isFavorite = favoriteProductRepository.exists(productWithProductImages.getId(), userId);

        eventBus.post(ProductViewEvent.from(productWithProductImages));

        return ProductDetailQueryDto.of(productWithProductImages, productQueryDto.getFavoriteCount(), chattingCount, isFavorite);
    }
}
