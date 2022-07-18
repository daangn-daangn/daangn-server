package com.daangndaangn.apiserver.controller.product;

import com.daangndaangn.apiserver.controller.product.ProductResponse.*;
import com.daangndaangn.apiserver.service.product.ProductService;
import com.daangndaangn.apiserver.service.product.query.ProductDetailQueryService;
import com.daangndaangn.apiserver.service.product.query.ProductQueryService;
import com.daangndaangn.apiserver.service.salereview.SaleReviewService;
import com.daangndaangn.apiserver.service.user.UserService;
import com.daangndaangn.common.api.entity.product.ProductImage;
import com.daangndaangn.common.api.entity.product.ProductState;
import com.daangndaangn.common.api.entity.user.User;
import com.daangndaangn.apiserver.service.product.query.ProductDetailQueryDto;
import com.daangndaangn.common.api.repository.product.query.ProductSearchOption;
import com.daangndaangn.common.error.UnauthorizedException;
import com.daangndaangn.common.jwt.JwtAuthentication;
import com.daangndaangn.common.util.PresignerUtils;
import com.daangndaangn.common.controller.ApiResult;
import com.daangndaangn.common.controller.ErrorResponseEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static com.daangndaangn.common.controller.ApiResult.OK;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;
import static org.springframework.http.HttpStatus.OK;

@RequestMapping("/api/products")
@RestController
@RequiredArgsConstructor
public class ProductApiController {

    private final ProductService productService;
    private final ProductQueryService productQueryService;
    private final ProductDetailQueryService productDetailService;
    private final UserService userService;
    private final SaleReviewService saleReviewService;
    private final PresignerUtils presignerUtils;

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

        List<SimpleResponse> products;

        if (user.isEmptyLocation()) {
            throw new IllegalStateException("물품 조회 시 위치 정보 등록이 필수입니다.");
        }

        products = productQueryService.getProducts(productSearchOption, user.getLocation(), pageable);

        products
            .stream()
            .filter(p -> isNotEmpty(p.getThumbNailImage()))
            .forEach(p -> p.updateImageUrl(presignerUtils.getProductPresignedGetUrl(p.getThumbNailImage())));

        return OK(products);
    }

    /**
     * 물품 상세 정보 조회
     *
     * GET /api/products/:productId
     */
    @GetMapping("/{productId}")
    public ApiResult<DetailResponse> getProduct(@AuthenticationPrincipal JwtAuthentication authentication,
                                                @PathVariable("productId") Long productId) {

        ProductDetailQueryDto productDetail = productDetailService.getProductDetail(productId, authentication.getId());

        List<String> productImageUrls = productDetail.getProductImages().stream()
                .map(ProductImage::getImageUrl)
                .map(presignerUtils::getProductPresignedGetUrl)
                .collect(toList());

        return OK(DetailResponse.from(productDetail, productImageUrls));
    }

    /**
     * 물품 등록
     *
     * POST /api/products
     *
     * success: CreateResponse
     */
    @PostMapping
    public CompletableFuture<ResponseEntity<ApiResult<?>>> createProduct(
                                                            @AuthenticationPrincipal JwtAuthentication authentication,
                                                            @Valid @RequestBody ProductRequest.CreateRequest request) {

        return productService.create(authentication.getId(),
                request.getCategoryId(),
                request.getTitle(),
                request.getPrice(),
                request.getDescription(),
                request.getProductImages()).handle((product, throwable) -> {

            if (product != null) {
                List<String> productImageUrls = product.getProductImages().stream()
                        .map(ProductImage::getImageUrl)
                        .map(presignerUtils::getProductPresignedPutUrl)
                        .collect(toList());

                return new ResponseEntity<>(OK(CreateResponse.from(product.getId(), productImageUrls)), OK);
            }

            return ErrorResponseEntity.from(throwable, true);
        });
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
                    request.getCategoryId(),
                    request.getPrice(),
                    request.getDescription());

            return OK();
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
            return OK();
        }

        throw new UnauthorizedException("물품 삭제는 판매자만 가능합니다.");
    }

    /**
     * 물품 상태 변경(0-숨기기,1-판매중,2-거래완료,3-예약중)
     *
     * PUT /api/products/state/:productId
     */
    @PutMapping("/state/{productId}")
    public ApiResult<Void> updateState(@PathVariable("productId") Long productId,
                                       @AuthenticationPrincipal JwtAuthentication authentication,
                                       @Valid @RequestBody ProductRequest.UpdateStateRequest request) {

        if (!productService.isSeller(productId, authentication.getId())) {
            throw new UnauthorizedException("물품 상태 변경은 판매자만 가능합니다.");
        }

        if (request.getState().equals(ProductState.SOLD_OUT.getCode())) {

            if (authentication.getId().equals(request.getBuyerId())) {
                throw new UnauthorizedException("자기 자신을 구매자로 물품 거래완료 처리를 할 수 없습니다.");
            }

            productService.updateToSoldOut(productId, request.getBuyerId());
        } else {
            productService.update(productId, request.getState());
        }

        return OK();
    }

    /**
     * 판매내역 조회
     *
     * GET /api/products/sales-history?state=0
     * GET /api/products/sales-history?state=1
     * GET /api/products/sales-history?state=2
     *
     * productState == 판매중/거래완료/숨김
     */
    @GetMapping("/sales-history")
    public ApiResult<List<SaleHistoryResponse>> getProductsBySeller(@RequestParam("state") int productState,
                                                                    @AuthenticationPrincipal JwtAuthentication authentication,
                                                                    @PageableDefault(size = 5) Pageable pageable) {

        List<SimpleResponse> products = productQueryService.getProductsBySeller(authentication.getId(),
                                                                                productState,
                                                                                pageable);

        products
            .stream()
            .filter(p -> isNotEmpty(p.getThumbNailImage()))
            .forEach(p -> p.updateImageUrl(presignerUtils.getProductPresignedGetUrl(p.getThumbNailImage())));

        if (ProductState.SOLD_OUT.getCode().equals(productState)) {
            List<SaleHistoryResponse> saleHistoryResponses =
                products.stream().map(product -> {
                    boolean hasReview = saleReviewService.existSellerReview(product.getId(), authentication.getId());
                    return SaleHistoryResponse.of(product, hasReview);
                }).collect(toList());

            return OK(saleHistoryResponses);
        }

        List<SaleHistoryResponse> saleHistoryResponses = products.stream()
                                                                .map(SaleHistoryResponse::from)
                                                                .collect(toList());

        return OK(saleHistoryResponses);
    }

    /**
     * 구매내역 조회
     *
     * GET /api/products/purchase-history
     */
    @GetMapping("/purchase-history")
    public ApiResult<List<PurchaseHistoryResponse>> getProductsByBuyer(
                                                            @AuthenticationPrincipal JwtAuthentication authentication,
                                                            @PageableDefault(size = 5) Pageable pageable) {

        List<SimpleResponse> products = productQueryService.getProductsByBuyer(authentication.getId(), pageable);

        products
            .stream()
            .filter(p -> isNotEmpty(p.getThumbNailImage()))
            .forEach(p -> p.updateImageUrl(presignerUtils.getProductPresignedGetUrl(p.getThumbNailImage())));

        List<PurchaseHistoryResponse> historyResponses =
            products.stream().map(product -> {
                boolean hasReview = saleReviewService.existBuyerReview(product.getId(), authentication.getId());
                return PurchaseHistoryResponse.of(product, hasReview);
            }).collect(toList());

        return OK(historyResponses);
    }

    /**
     * 물품 끌어올리기
     *
     * PUT /api/products/refreshment/:productId
     */
    @PutMapping("/refreshment/{productId}")
    public ApiResult<Void> refreshProduct(@PathVariable("productId") Long productId,
                                          @AuthenticationPrincipal JwtAuthentication authentication) {

        if (!productService.isSeller(productId, authentication.getId())) {
            throw new UnauthorizedException("물품 끌어올리기는 판매자만 가능합니다.");
        }

        productService.refresh(productId);

        return OK();
    }
}
