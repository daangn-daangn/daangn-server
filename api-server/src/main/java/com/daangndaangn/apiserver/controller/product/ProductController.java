package com.daangndaangn.apiserver.controller.product;

import com.daangndaangn.apiserver.controller.ApiResult;
import com.daangndaangn.apiserver.controller.product.ProductResponse.CreateResponse;
import com.daangndaangn.apiserver.controller.product.ProductResponse.SimpleResponse;
import com.daangndaangn.apiserver.error.UnauthorizedException;
import com.daangndaangn.apiserver.security.jwt.JwtAuthentication;
import com.daangndaangn.apiserver.service.product.ProductService;
import com.daangndaangn.apiserver.service.product.query.ProductQueryService;
import com.daangndaangn.apiserver.service.user.UserService;
import com.daangndaangn.common.api.entity.user.User;
import com.daangndaangn.common.api.repository.product.query.ProductSearchOption;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.List;

import static com.daangndaangn.apiserver.controller.ApiResult.OK;

@RequestMapping("/api/products")
@RestController
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final ProductQueryService productQueryService;
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
        return OK(productQueryService.getProducts(productSearchOption, user.getLocation(), pageable));
    }

    //TODO
    /**
     * 물품 상세 정보 조회
     */

    /**
     * 물품 등록
     *
     * POST /api/products
     */
    @PostMapping
    public ApiResult<CreateResponse> createProduct(@AuthenticationPrincipal JwtAuthentication authentication,
                                                   @Valid @RequestBody ProductRequest.CreateRequest request) {

        Long productId = productService.create(authentication.getId(),
                request.getCategoryId(),
                request.getTitle(),
                request.getName(),
                request.getPrice(),
                request.getDescription());

        return OK(CreateResponse.from(productId));
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
