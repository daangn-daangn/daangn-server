package com.daangndaangn.apiserver;

import com.daangndaangn.apiserver.controller.product.ProductResponse;
import com.daangndaangn.apiserver.service.product.query.ProductQueryService;
import com.daangndaangn.common.api.repository.product.query.ProductQueryDto;
import com.daangndaangn.common.api.repository.product.query.ProductQueryRepository;
import com.daangndaangn.common.api.repository.product.query.ProductSearchOption;
import com.daangndaangn.common.chat.document.ChattingInfo;
import com.daangndaangn.common.chat.repository.ChattingInfoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RequestMapping("/api/test")
@RequiredArgsConstructor
@RestController
public class TestController {

    private final ProductQueryService productQueryService;
    private final ProductQueryRepository productQueryRepository;
    private final ChattingInfoRepository chattingInfoRepository;

    @GetMapping
    public void func000() {
        ChattingInfo chattingInfo1 = ChattingInfo.builder().chattingId("1")
                .productId(1L)
                .sellerId(1L)
                .buyerId(2L)
                .build();

        ChattingInfo chattingInfo2 = ChattingInfo.builder().chattingId("2")
                .productId(1L)
                .sellerId(1L)
                .buyerId(3L)
                .build();

        ChattingInfo chattingInfo3 = ChattingInfo.builder().chattingId("3")
                .productId(1L)
                .sellerId(1L)
                .buyerId(4L)
                .build();

        ChattingInfo chattingInfo4 = ChattingInfo.builder().chattingId("4")
                .productId(1L)
                .sellerId(1L)
                .buyerId(5L)
                .build();

        chattingInfoRepository.save(chattingInfo1);
        chattingInfoRepository.save(chattingInfo2);
        chattingInfoRepository.save(chattingInfo3);
        chattingInfoRepository.save(chattingInfo4);
    }

    @GetMapping("/v1")
    public List<ProductQueryDto> func(ProductSearchOption productSearchOption) {
        return productQueryRepository.findAll(productSearchOption, PageRequest.of(0, 20));
    }

    @GetMapping("/v2")
    public List<ProductResponse.SimpleResponse> func2(ProductSearchOption productSearchOption) {
        return productQueryService.getProducts(productSearchOption, PageRequest.of(0, 20));
    }
}
