package com.daangndaangn.apiserver.controller.product;

import com.daangndaangn.apiserver.controller.product.ProductResponse.CreateResponse;
import com.daangndaangn.apiserver.controller.product.ProductResponse.DetailResponse;
import com.daangndaangn.apiserver.controller.product.ProductResponse.SimpleResponse;
import com.daangndaangn.apiserver.security.jwt.JwtAuthentication;
import com.daangndaangn.apiserver.service.product.ProductService;
import com.daangndaangn.apiserver.service.product.query.ProductQueryService;
import com.daangndaangn.apiserver.service.user.UserService;
import com.daangndaangn.common.api.entity.product.Product;
import com.daangndaangn.common.api.entity.product.ProductImage;
import com.daangndaangn.common.api.entity.user.User;
import com.daangndaangn.common.api.repository.product.query.ProductSearchOption;
import com.daangndaangn.common.error.UnauthorizedException;
import com.daangndaangn.common.util.PresignerUtils;
import com.daangndaangn.common.web.ApiResult;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.List;

import static com.daangndaangn.common.web.ApiResult.OK;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

@RequestMapping("/api/products")
@RestController
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final ProductQueryService productQueryService;
    private final PresignerUtils presignerUtils;
    private final UserService userService;

    /**
     * 물품 리스트 조회(회원 전용)
     *
     * GET /api/products
     * GET /api/products?title=맥북
     * GET /api/products?category=1,2,3,4,5
     * GET /api/products?title=맥북&category=1,2,3,4,5
     */
    @GetMapping
    public ApiResult<List<SimpleResponse>> getProducts(@AuthenticationPrincipal JwtAuthentication authentication,
                                                       ProductSearchOption productSearchOption,
                                                       @PageableDefault(size = 5) Pageable pageable) {

        User user = userService.getUser(authentication.getId());

        List<SimpleResponse> products = productQueryService.getProducts(productSearchOption, user.getLocation(), pageable);

        products
            .stream()
            .filter(p -> isNotEmpty(p.getImageUrl()))
            .forEach(p -> p.updateImageUrl(presignerUtils.getProductPresignedGetUrl(p.getImageUrl())));

        return OK(products);
    }

    /**
     * 물품 상세 정보 조회
     *
     * GET /api/products/:productId
     */
    @GetMapping("/{productId}")
    public ApiResult<DetailResponse> getProduct(@PathVariable("productId") Long productId) {

        Product product = productService.getProductWithProductImages(productId);

        List<String> productImageUrls = product.getProductImages().stream()
                .map(ProductImage::getImageUrl)
                .map(presignerUtils::getProductPresignedGetUrl)
                .collect(toList());

        return OK(DetailResponse.from(product, productImageUrls));
    }

    /**
     * 물품 등록
     *
     * POST /api/products
     */
    @PostMapping
    public ApiResult<CreateResponse> createProduct(@AuthenticationPrincipal JwtAuthentication authentication,
                                                   @Valid @RequestBody ProductRequest.CreateRequest request) {

        Product product = productService.create(authentication.getId(),
                request.getCategoryId(),
                request.getTitle(),
                request.getName(),
                request.getPrice(),
                request.getDescription(),
                request.getProductImages());

        List<String> productImageUrls = product.getProductImages().stream()
                                            .map(ProductImage::getImageUrl)
                                            .map(presignerUtils::getProductPresignedPutUrl)
                                            .collect(toList());

        return OK(CreateResponse.from(product.getId(), productImageUrls));
    }

    /**
     * 물품 수정
     *
     * PUT /api/products/:productId
     */
    @PutMapping("/{productId}")
    public ApiResult<Void> updateProduct(@PathVariable("productId") Long productId,
                                         @AuthenticationPrincipal JwtAuthentication authentication,
                                         @Valid @RequestBody ProductRequest.UpdateRequest request) {

        if (productService.isSeller(productId, authentication.getId())) {
            productService.update(productId,
                    request.getTitle(),
                    request.getName(),
                    request.getCategoryId(),
                    request.getPrice(),
                    request.getDescription());

            return OK(null);
        }

        throw new UnauthorizedException("물품 수정은 판매자만 가능합니다.");
    }

    /**
     * 물품 삭제
     *
     * DELETE /api/products/:productId
     */
    @DeleteMapping("/{productId}")
    public ApiResult<Void> deleteProduct(@PathVariable("productId") Long productId,
                                         @AuthenticationPrincipal JwtAuthentication authentication) {

        if (productService.isSeller(productId, authentication.getId())) {
            productService.delete(productId);
            return OK(null);
        }

        throw new UnauthorizedException("물품 삭제는 판매자만 가능합니다.");
    }

    /**
     * 물품 판매 완료 처리
     *
     * PUT /api/products/sold-out/:productId
     */
    @PutMapping("/sold-out/{productId}")
    public ApiResult<Void> updateSoldOutProduct(@PathVariable("productId") Long productId,
                                         @AuthenticationPrincipal JwtAuthentication authentication,
                                         @Valid @RequestBody ProductRequest.SoldOutRequest request) {

        if (authentication.getId() != null && authentication.getId().equals(request.getBuyerId())) {
            throw new UnauthorizedException("자기 자신을 구매자로 물품 판매 완료 처리를 할 수 없습니다.");
        }

        if (productService.isSeller(productId, authentication.getId())) {
            productService.updateToSoldOut(productId, request.getBuyerId());
            return OK(null);
        }

        throw new UnauthorizedException("물품 판매 완료 처리는 판매자만 가능합니다.");
    }
}
