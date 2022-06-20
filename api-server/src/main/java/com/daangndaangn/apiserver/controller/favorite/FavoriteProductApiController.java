package com.daangndaangn.apiserver.controller.favorite;

import com.daangndaangn.apiserver.controller.favorite.FavoriteProductRequest.CreateRequest;
import com.daangndaangn.apiserver.controller.favorite.FavoriteProductResponse.CreateResponse;
import com.daangndaangn.apiserver.controller.product.ProductResponse.SimpleResponse;
import com.daangndaangn.apiserver.service.favorite.FavoriteProductService;
import com.daangndaangn.apiserver.service.product.query.ProductQueryService;
import com.daangndaangn.common.error.UnauthorizedException;
import com.daangndaangn.common.jwt.JwtAuthentication;
import com.daangndaangn.common.web.ApiResult;
import com.daangndaangn.common.web.ErrorResponseEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static com.daangndaangn.common.web.ApiResult.OK;
import static java.util.stream.Collectors.toList;
import static org.springframework.http.HttpStatus.OK;

@RequestMapping("/api/favorite-products")
@RestController
@RequiredArgsConstructor
public class FavoriteProductApiController {

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
     *
     * success: CreateResponse
     */
    @PostMapping
    public CompletableFuture<ResponseEntity<ApiResult<?>>> createFavoriteProduct(
                                                            @AuthenticationPrincipal JwtAuthentication authentication,
                                                            @Valid @RequestBody CreateRequest request) {

        return favoriteProductService.create(request.getProductId(), authentication.getId()).handle((id, throwable) -> {
            if (id != null) {
                return new ResponseEntity<>(OK(CreateResponse.from(id)), OK);
            }

            return ErrorResponseEntity.from(throwable, true);
        });
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
