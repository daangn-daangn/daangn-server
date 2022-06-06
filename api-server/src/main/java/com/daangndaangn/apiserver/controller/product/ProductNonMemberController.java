package com.daangndaangn.apiserver.controller.product;

import com.daangndaangn.apiserver.controller.product.ProductResponse.SimpleResponse;
import com.daangndaangn.apiserver.service.product.query.ProductQueryService;
import com.daangndaangn.common.api.repository.product.query.ProductSearchOption;
import com.daangndaangn.common.util.PresignerUtils;
import com.daangndaangn.common.web.ApiResult;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.daangndaangn.common.web.ApiResult.OK;


@RequestMapping("/api/non-member/products")
@RestController
@RequiredArgsConstructor
public class ProductNonMemberController {

    private final ProductQueryService productQueryService;
    private final PresignerUtils presignerUtils;

    /**
     * 물품 리스트 조회(비회원 전용)
     *
     * GET /api/non-member/products
     * GET /api/non-member/products?title=맥북
     * GET /api/non-member/products?category=1,2,3,4,5
     * GET /api/non-member/products?title=맥북&category=1,2,3,4,5
     */
    @GetMapping
    public ApiResult<List<SimpleResponse>> getProducts(ProductSearchOption productSearchOption,
                                                       @PageableDefault(size = 5) Pageable pageable) {

        List<SimpleResponse> products = productQueryService.getProducts(productSearchOption, pageable);

        products
            .stream()
            .filter(p -> StringUtils.isNotEmpty(p.getImageUrl()))
            .forEach(p -> p.updateImageUrl(presignerUtils.getProductPresignedGetUrl(p.getImageUrl())));

        return OK(products);
    }
}
