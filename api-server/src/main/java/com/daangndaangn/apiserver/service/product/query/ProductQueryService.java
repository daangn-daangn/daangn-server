package com.daangndaangn.apiserver.service.product.query;

import com.daangndaangn.apiserver.controller.product.ProductResponse;
import com.daangndaangn.common.api.entity.user.Location;
import com.daangndaangn.common.api.repository.product.query.ProductQueryDto;
import com.daangndaangn.common.api.repository.product.query.ProductQueryRepository;
import com.daangndaangn.common.api.repository.product.query.ProductSearchOption;
import com.daangndaangn.common.chat.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.stream.Collectors.toList;


@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class ProductQueryService {

    private final ProductQueryRepository productQueryRepository;
    private final ChatRoomRepository chatRoomRepository;

    //회원 전용
    public List<ProductResponse.SimpleResponse> getProducts(ProductSearchOption productSearchOption, Location location, Pageable pageable) {
        List<ProductQueryDto> productQueryDtos = productQueryRepository.findAll(productSearchOption, location.getAddress(), pageable);

        return productQueryDtos.stream().map(p -> {
            long chattingCount = chatRoomRepository.countAllByProductId(p.getId());
            return ProductResponse.SimpleResponse.of(p, chattingCount);
        }).collect(toList());
    }

    //비회원 전용
    public List<ProductResponse.SimpleResponse> getProducts(ProductSearchOption productSearchOption, Pageable pageable) {
        List<ProductQueryDto> productQueryDtos = productQueryRepository.findAll(productSearchOption, null, pageable);

        return productQueryDtos.stream().map(p -> {
            long chattingCount = chatRoomRepository.countAllByProductId(p.getId());
            return ProductResponse.SimpleResponse.of(p, chattingCount);
        }).collect(toList());
    }

    //특정 사용자가 찜한 상품 목록만 조회
    public List<ProductResponse.SimpleResponse> getFavoriteProducts(List<Long> productIds) {
        List<ProductQueryDto> productQueryDtos = productQueryRepository.findAll(productIds);

        return productQueryDtos.stream().map(p -> {
            long chattingCount = chatRoomRepository.countAllByProductId(p.getId());
            return ProductResponse.SimpleResponse.of(p, chattingCount);
        }).collect(toList());
    }
}
