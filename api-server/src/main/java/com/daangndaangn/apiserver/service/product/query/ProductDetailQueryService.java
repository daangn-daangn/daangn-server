package com.daangndaangn.apiserver.service.product.query;

import com.daangndaangn.apiserver.service.product.ProductService;
import com.daangndaangn.common.api.entity.product.Product;
import com.daangndaangn.common.api.repository.product.query.ProductQueryDto;
import com.daangndaangn.common.api.repository.product.query.ProductQueryRepository;
import com.daangndaangn.common.chat.repository.chatroom.ChatRoomRepository;
import com.daangndaangn.common.error.NotFoundException;
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

    public ProductDetailQueryDto getProductDetail(Long id) {

        ProductQueryDto productQueryDto = productQueryRepository.findById(id)
            .orElseThrow(() -> new NotFoundException(ProductQueryDto.class, String.format("productId = %s", id)));

        Product productWithProductImages = productService.getProductWithProductImages(id);
        long chattingCount = chatRoomRepository.countAllByProductId(id);

        return ProductDetailQueryDto.of(productWithProductImages, productQueryDto.getFavoriteCount(), chattingCount);
    }
}
