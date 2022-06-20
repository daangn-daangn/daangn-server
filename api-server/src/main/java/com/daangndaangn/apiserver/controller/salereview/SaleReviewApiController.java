package com.daangndaangn.apiserver.controller.salereview;

import com.daangndaangn.apiserver.controller.salereview.SaleReviewRequest.BuyerReviewCreateRequest;
import com.daangndaangn.apiserver.controller.salereview.SaleReviewRequest.SellerReviewCreateRequest;
import com.daangndaangn.apiserver.controller.salereview.SaleReviewResponse.CreateResponse;
import com.daangndaangn.apiserver.controller.salereview.SaleReviewResponse.GetResponse;
import com.daangndaangn.common.api.entity.review.SaleReview;
import com.daangndaangn.apiserver.service.salereview.SaleReviewService;
import com.daangndaangn.common.error.UnauthorizedException;
import com.daangndaangn.common.jwt.JwtAuthentication;
import com.daangndaangn.common.util.PresignerUtils;
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

import static com.daangndaangn.common.api.entity.review.SaleReviewType.BUYER_REVIEW;
import static com.daangndaangn.common.api.entity.review.SaleReviewType.SELLER_REVIEW;
import static com.daangndaangn.common.web.ApiResult.OK;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.springframework.http.HttpStatus.OK;

@RequestMapping("/api/sale-reviews")
@RestController
@RequiredArgsConstructor
public class SaleReviewApiController {

    private final PresignerUtils presignerUtils;
    private final SaleReviewService saleReviewService;

    /**
     * GET /api/sale-reviews/:sale-review-id
     *
     * SaleReview 단 건 조회
     */
    @GetMapping("/{sale-review-id}")
    public ApiResult<GetResponse> getSaleReview(@PathVariable("sale-review-id") Long saleReviewId) {
        SaleReview saleReview = saleReviewService.getSaleReview(saleReviewId);
        String profileUrl = presignerUtils.getProfilePresignedGetUrl(saleReview.getReviewer().getProfileUrl());

        return OK(GetResponse.of(saleReview, profileUrl));
    }

    /**
     * GET /api/sale-reviews/user/:user-id
     *
     * 사용자의 전체 후기 조회(SaleReview 리스트 조회)
     */
    @GetMapping("/user/{user-id}")
    public ApiResult<List<GetResponse>> getSaleReviews(@PathVariable("user-id") Long userId, Pageable pageable) {

        List<GetResponse> userReviews =
                saleReviewService.getUserReviews(userId, pageable)
                        .stream()
                        .map(saleReview -> {
                            String profileUrl = isEmpty(saleReview.getReviewer().getProfileUrl()) ?
                                null : presignerUtils.getProfilePresignedGetUrl(saleReview.getReviewer().getProfileUrl());
                            return GetResponse.of(saleReview, profileUrl);
                        })
                        .collect(toList());

        return OK(userReviews);
    }

    /**
     * GET /api/sale-reviews/seller/:user-id
     *
     * 사용자의 판매자 후기 조회(SaleReview 리스트 조회)
     */
    @GetMapping("/seller/{user-id}")
    public ApiResult<List<GetResponse>> getSellerReviews(@PathVariable("user-id") Long userId, Pageable pageable) {

        List<GetResponse> sellerReviews =
                saleReviewService.getSellerReviews(userId, pageable)
                        .stream()
                        .map(saleReview -> {
                            String profileUrl = isEmpty(saleReview.getReviewer().getProfileUrl()) ?
                                null : presignerUtils.getProfilePresignedGetUrl(saleReview.getReviewer().getProfileUrl());
                            return GetResponse.of(saleReview, profileUrl);
                        })
                        .collect(toList());

        return OK(sellerReviews);
    }

    /**
     * GET /api/sale-reviews/buyer/:user-id
     *
     * 사용자의 구매자 후기 조회(SaleReview 리스트 조회)
     */
    @GetMapping("/buyer/{user-id}")
    public ApiResult<List<GetResponse>> getBuyerReviews(@PathVariable("user-id") Long userId, Pageable pageable) {

        List<GetResponse> buyerReviews =
                saleReviewService.getBuyerReviews(userId, pageable)
                        .stream()
                        .map(saleReview -> {
                            String profileUrl = isEmpty(saleReview.getReviewer().getProfileUrl()) ?
                                null : presignerUtils.getProfilePresignedGetUrl(saleReview.getReviewer().getProfileUrl());
                            return GetResponse.of(saleReview, profileUrl);
                        })
                        .collect(toList());

        return OK(buyerReviews);
    }

    /**
     * POST /api/sale-reviews/seller
     *
     * 사용자는 판매자 후기를 남길 수 있다.
     *
     * success: CreateResponse
     */
    @PostMapping("/seller")
    public CompletableFuture<ResponseEntity<ApiResult<?>>> createSellerReview(
                                                            @AuthenticationPrincipal JwtAuthentication authentication,
                                                            @Valid @RequestBody SellerReviewCreateRequest request) {

        Long productId = request.getProductId();
        Long reviewerId = authentication.getId();
        Long revieweeId = request.getBuyerId();
        String content = request.getContent();

        return saleReviewService.create(productId,reviewerId,revieweeId,SELLER_REVIEW,content).handle((id, throwable) -> {
            if (id != null) {
                return new ResponseEntity<>(OK(CreateResponse.from(id)), OK);
            }

            return ErrorResponseEntity.from(throwable, true);
        });
    }

    /**
     * POST /api/sale-reviews/buyer
     *
     * 사용자는 구매자 후기를 남길 수 있다.
     *
     * success: CreateResponse
     */
    @PostMapping("/buyer")
    public CompletableFuture<ResponseEntity<ApiResult<?>>> createBuyerReview(
                                                            @AuthenticationPrincipal JwtAuthentication authentication,
                                                            @Valid @RequestBody BuyerReviewCreateRequest request) {

        Long productId = request.getProductId();
        Long reviewerId = authentication.getId();
        Long revieweeId = request.getSellerId();
        String content = request.getContent();

        return saleReviewService.create(productId,reviewerId,revieweeId,BUYER_REVIEW,content).handle((id, throwable) -> {
            if (id != null) {
                return new ResponseEntity<>(OK(CreateResponse.from(id)), OK);
            }

            return ErrorResponseEntity.from(throwable, true);
        });
    }

    /**
     * PUT /api/sale-reviews/seller/:sale-review-id
     *
     * 사용자는 판매자 후기를 수정할 수 있다.
     */
    @PutMapping("/seller/{sale-review-id}")
    public ApiResult<Void> updateSellerReview(@PathVariable("sale-review-id") Long saleReviewId,
                                              @AuthenticationPrincipal JwtAuthentication authentication,
                                              @Valid @RequestBody SaleReviewRequest.UpdateRequest request) {

        SaleReview saleReview = saleReviewService.getSaleReview(saleReviewId);

        if (authentication.getId().equals(saleReview.getReviewer().getId())) {
            saleReviewService.update(saleReviewId, request.getContent());
            return OK(null);
        }

        throw new UnauthorizedException("리뷰 수정은 작성자만 가능합니다.");
    }

    /**
     * PUT /api/sale-reviews/buyer/:sale-review-id
     *
     * 사용자는 구매자 후기를 수정할 수 있다.
     */
    @PutMapping("/buyer/{sale-review-id}")
    public ApiResult<Void> updateBuyerReview(@PathVariable("sale-review-id") Long saleReviewId,
                                             @AuthenticationPrincipal JwtAuthentication authentication,
                                             @Valid @RequestBody SaleReviewRequest.UpdateRequest request) {

        SaleReview saleReview = saleReviewService.getSaleReview(saleReviewId);

        if (authentication.getId().equals(saleReview.getReviewer().getId())) {
            saleReviewService.update(saleReviewId, request.getContent());
            return OK(null);
        }

        throw new UnauthorizedException("리뷰 수정은 작성자만 가능합니다.");
    }

    /**
     * PUT /api/sale-reviews/hide/:sale-review-id
     *
     * 사용자는 자신이 받은 후기를 숨길 수 있다.
     */
    @PutMapping("/hide/{sale-review-id}")
    public ApiResult<Void> hideSaleReview(@PathVariable("sale-review-id") Long saleReviewId,
                                          @AuthenticationPrincipal JwtAuthentication authentication) {

        saleReviewService.hide(saleReviewId, authentication.getId());

        return OK(null);
    }

    /**
     * DELETE /api/sale-reviews/seller/:sale-review-id
     *
     * 사용자는 판매자 후기를 삭제할 수 있다.
     */
    @DeleteMapping("/seller/{sale-review-id}")
    public ApiResult<Void> deleteSellerReview(@PathVariable("sale-review-id") Long saleReviewId,
                                              @AuthenticationPrincipal JwtAuthentication authentication) {

        SaleReview saleReview = saleReviewService.getSaleReview(saleReviewId);

        if (authentication.getId().equals(saleReview.getReviewer().getId())) {
            saleReviewService.delete(saleReviewId);
            return OK(null);
        }

        throw new UnauthorizedException("리뷰 삭제는 작성자만 가능합니다.");
    }

    /**
     * DELETE /api/sale-reviews/buyer/:sale-review-id
     *
     * 사용자는 구매자 후기를 삭제할 수 있다.
     */
    @DeleteMapping("/buyer/{sale-review-id}")
    public ApiResult<Void> deleteBuyerReview(@PathVariable("sale-review-id") Long saleReviewId,
                                             @AuthenticationPrincipal JwtAuthentication authentication) {

        SaleReview saleReview = saleReviewService.getSaleReview(saleReviewId);

        if (authentication.getId().equals(saleReview.getReviewer().getId())) {
            saleReviewService.delete(saleReviewId);
            return OK(null);
        }

        throw new UnauthorizedException("리뷰 삭제는 작성자만 가능합니다.");
    }
}
