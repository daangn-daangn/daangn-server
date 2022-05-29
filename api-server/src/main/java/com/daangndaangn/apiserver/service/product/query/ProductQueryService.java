package com.daangndaangn.apiserver.service.product.query;

import com.daangndaangn.apiserver.controller.product.ProductResponse;
import com.daangndaangn.common.api.repository.product.query.ProductQueryDto;
import com.daangndaangn.common.api.repository.product.query.ProductQueryRepository;
import com.daangndaangn.common.api.repository.product.query.ProductSearchOption;
import com.daangndaangn.common.chat.repository.ChattingInfoRepository;
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
    private final ChattingInfoRepository chattingInfoRepository;

    public List<ProductResponse.SimpleResponse> getProducts(ProductSearchOption productSearchOption, Pageable pageable) {
        List<ProductQueryDto> productQueryDtos = productQueryRepository.findAll(productSearchOption, pageable);

        return productQueryDtos.stream().map(p -> {
            long chattingCount = chattingInfoRepository.countAllByProductId(p.getId());
            return ProductResponse.SimpleResponse.of(p, chattingCount);
        }).collect(toList());
    }
}
