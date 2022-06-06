package com.daangndaangn.apiserver.controller.favorite;

import com.daangndaangn.apiserver.controller.favorite.FavoriteProductRequest.CreateRequest;
import com.daangndaangn.apiserver.controller.favorite.FavoriteProductResponse.CreateResponse;
import com.daangndaangn.apiserver.controller.product.ProductResponse.SimpleResponse;
import com.daangndaangn.apiserver.security.jwt.JwtAuthentication;
import com.daangndaangn.apiserver.service.favorite.FavoriteProductService;
import com.daangndaangn.apiserver.service.product.query.ProductQueryService;
import com.daangndaangn.common.error.UnauthorizedException;
import com.daangndaangn.common.web.ApiResult;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.List;

import static com.daangndaangn.common.web.ApiResult.OK;
import static java.util.stream.Collectors.toList;


@RequestMapping("/api/favorite-products")
@RestController
@RequiredArgsConstructor
public class FavoriteProductController {

    private final FavoriteProductService favoriteProductService;
    private final ProductQueryService productQueryService;

    /**
     * 찜한 상품 목록 조회
     *
     * GET /api/favorite-products
     */
    @GetMapping
    public ApiResult<List<SimpleResponse>> getFavoriteProducts(@AuthenticationPrincipal JwtAuthentication authentication,
                                                               Pageable pageable) {

        List<Long> productIds = favoriteProductService.getFavoriteProducts(authentication.getId(), pageable)
                .stream()
                .map(favoriteProduct -> favoriteProduct.getProduct().getId())
                .collect(toList());

        return OK(productQueryService.getFavoriteProducts(productIds));
    }

    /**
     * 찜하기 등록
     *
     * POST /api/favorite-products
     */
    @PostMapping
    public ApiResult<CreateResponse> createFavoriteProduct(@AuthenticationPrincipal JwtAuthentication authentication,
                                                           @Valid @RequestBody CreateRequest request) {

        Long id = favoriteProductService.create(request.getProductId(), authentication.getId());

        return OK(CreateResponse.from(id));
    }

    /**
     * 찜하기 삭제
     *
     * DELETE /api/favorite-products/:favoriteId
     */
    @DeleteMapping("/{favoriteId}")
    public ApiResult<Void> deleteFavoriteProduct(@PathVariable("favoriteId") Long favoriteId,
                                                 @AuthenticationPrincipal JwtAuthentication authentication) {

        if (favoriteProductService.isOwner(favoriteId, authentication.getId())) {
            favoriteProductService.delete(favoriteId);
            return OK(null);
        }

        throw new UnauthorizedException("찜하기를 누른 본인만 찜하기를 삭제할 수 있습니다.");
    }
}
