package com.daangndaangn.apiserver.controller.salereview;

import com.daangndaangn.apiserver.controller.ApiResult;
import com.daangndaangn.apiserver.entity.review.SaleReview;
import com.daangndaangn.apiserver.error.UnauthorizedException;
import com.daangndaangn.apiserver.security.jwt.JwtAuthentication;
import com.daangndaangn.apiserver.service.salereview.SaleReviewService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@RequestMapping("/api/sale-reviews")
@RestController
@RequiredArgsConstructor
public class SaleReviewController {

    private final SaleReviewService saleReviewService;

    /**
     * GET /api/sale-reviews/{sale-review-id}
     *
     * SaleReview 단 건 조회
     */
    @GetMapping("/{sale-review-id}")
    public ApiResult<SaleReviewResponse.GetResponse> getSaleReview(@PathVariable("sale-review-id") Long saleReviewId) {
        return ApiResult.OK(saleReviewService.getSaleReview(saleReviewId));
    }

    /**
     * GET /api/sale-reviews/user/{user-id}
     *
     * 사용자의 전체 후기 조회(SaleReview 리스트 조회)
     */
    @GetMapping("/user/{user-id}")
    public ApiResult<SaleReviewResult> getSaleReviews(@PathVariable("user-id") Long userId, Pageable pageable) {

        List<SaleReviewResponse.GetResponse> allUserReviews = saleReviewService.getAllUserReview(userId, pageable);

        return ApiResult.OK(SaleReviewResult.of(allUserReviews.size(), allUserReviews));
    }

    /**
     * GET /api/sale-reviews/seller/{user-id}
     *
     * 사용자의 판매자 후기 조회(SaleReview 리스트 조회)
     */
    @GetMapping("/seller/{user-id}")
    public ApiResult<SaleReviewResult> getSellerReviews(@PathVariable("user-id") Long userId, Pageable pageable) {

        List<SaleReviewResponse.GetResponse> allSellerReviews = saleReviewService.getAllSellerReview(userId, pageable);

        return ApiResult.OK(SaleReviewResult.of(allSellerReviews.size(), allSellerReviews));
    }

    /**
     * GET /api/sale-reviews/buyer/{user-id}
     *
     * 사용자의 구매자 후기 조회(SaleReview 리스트 조회)
     */
    @GetMapping("/buyer/{user-id}")
    public ApiResult<SaleReviewResult> getBuyerReviews(@PathVariable("user-id") Long userId, Pageable pageable) {

        List<SaleReviewResponse.GetResponse> allBuyerReviews = saleReviewService.getAllBuyerReview(userId, pageable);

        return ApiResult.OK(SaleReviewResult.of(allBuyerReviews.size(), allBuyerReviews));
    }

    @Getter
    @AllArgsConstructor
    static class SaleReviewResult {
        private int dataSize;
        private List<SaleReviewResponse.GetResponse> data;

        static SaleReviewResult of(int dataSize, List<SaleReviewResponse.GetResponse> data) {
            return new SaleReviewResult(dataSize, data);
        }
    }

    /**
     * POST /api/sale-reviews/seller
     *
     * 사용자는 판매자 후기를 남길 수 있다.
     */
    @PostMapping("/seller")
    public ApiResult<Long> createSellerReview(@AuthenticationPrincipal JwtAuthentication authentication,
                                              @Valid @RequestBody SaleReviewRequest.CreateRequest request) {

        if (authentication.getId().equals(request.getSellerId())) {
            Long saleReviewId = saleReviewService.createSaleReview(request);
            return ApiResult.OK(saleReviewId);
        }

        throw new UnauthorizedException("판매자는 본인이어야 합니다.");
    }

    /**
     * POST /api/sale-reviews/buyer
     *
     * 사용자는 구매자 후기를 남길 수 있다.
     */
    @PostMapping("/buyer")
    public ApiResult<Long> createBuyerReview(@AuthenticationPrincipal JwtAuthentication authentication,
                                             @Valid @RequestBody SaleReviewRequest.CreateRequest request) {

        if (authentication.getId().equals(request.getBuyerId())) {
            Long saleReviewId = saleReviewService.createSaleReview(request);
            return ApiResult.OK(saleReviewId);
        }

        throw new UnauthorizedException("구매자는 본인이어야 합니다.");
    }

    /**
     * PUT /api/sale-reviews/seller/{sale-review-id}
     *
     * 사용자는 판매자 후기를 수정할 수 있다.
     */
    @PutMapping("/seller/{sale-review-id}")
    public ApiResult<Void> updateSellerReview(@PathVariable("sale-review-id") Long saleReviewId,
                                              @AuthenticationPrincipal JwtAuthentication authentication,
                                              @Valid @RequestBody SaleReviewRequest.UpdateRequest request) {

        SaleReview saleReview = saleReviewService.findSaleReview(saleReviewId);

        if (authentication.getId().equals(saleReview.getSeller().getId())) {
            saleReviewService.updateSaleReview(saleReviewId, request);
            return ApiResult.OK(null);
        }

        throw new UnauthorizedException("리뷰 수정은 작성자만 가능합니다.");
    }

    /**
     * PUT /api/sale-reviews/buyer/{sale-review-id}
     *
     * 사용자는 구매자 후기를 수정할 수 있다.
     */
    @PutMapping("/buyer/{sale-review-id}")
    public ApiResult<Void> updateBuyerReview(@PathVariable("sale-review-id") Long saleReviewId,
                                             @AuthenticationPrincipal JwtAuthentication authentication,
                                             @Valid @RequestBody SaleReviewRequest.UpdateRequest request) {

        SaleReview saleReview = saleReviewService.findSaleReview(saleReviewId);

        if (authentication.getId().equals(saleReview.getBuyer().getId())) {
            saleReviewService.updateSaleReview(saleReviewId, request);
            return ApiResult.OK(null);
        }

        throw new UnauthorizedException("리뷰 수정은 작성자만 가능합니다.");
    }

    /**
     * DELETE /api/sale-reviews/seller/{sale-review-id}
     *
     * 사용자는 판매자 후기를 삭제할 수 있다.
     */
    @DeleteMapping("/seller/{sale-review-id}")
    public ApiResult<Void> deleteSellerReview(@PathVariable("sale-review-id") Long saleReviewId,
                                              @AuthenticationPrincipal JwtAuthentication authentication) {

        SaleReview saleReview = saleReviewService.findSaleReview(saleReviewId);

        if (authentication.getId().equals(saleReview.getSeller().getId())) {
            saleReviewService.delete(saleReviewId);
            return ApiResult.OK(null);
        }

        throw new UnauthorizedException("리뷰 삭제는 작성자만 가능합니다.");
    }

    /**
     * DELETE /api/sale-reviews/buyer/{sale-review-id}
     *
     * 사용자는 구매자 후기를 삭제할 수 있다.
     */
    @DeleteMapping("/buyer/{sale-review-id}")
    public ApiResult<Void> deleteBuyerReview(@PathVariable("sale-review-id") Long saleReviewId,
                                             @AuthenticationPrincipal JwtAuthentication authentication) {

        SaleReview saleReview = saleReviewService.findSaleReview(saleReviewId);

        if (authentication.getId().equals(saleReview.getBuyer().getId())) {
            saleReviewService.delete(saleReviewId);
            return ApiResult.OK(null);
        }

        throw new UnauthorizedException("리뷰 삭제는 작성자만 가능합니다.");
    }
}