package com.daangndaangn.apiserver.service.salereview;

import com.daangndaangn.apiserver.service.product.ProductService;
import com.daangndaangn.common.api.entity.product.Product;
import com.daangndaangn.common.api.entity.review.SaleReview;
import com.daangndaangn.common.api.entity.review.SaleReviewType;
import com.daangndaangn.common.api.entity.user.User;
import com.daangndaangn.common.api.repository.salereview.SaleReviewRepository;
import com.daangndaangn.apiserver.service.user.UserService;
import com.daangndaangn.common.error.NotFoundException;
import com.daangndaangn.common.error.UnauthorizedException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;
import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;


@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class SaleReviewServiceImpl implements SaleReviewService {

    private final SaleReviewRepository saleReviewRepository;
    private final ProductService productService;
    private final UserService userService;

    @Override
    @Transactional
    public Long create(Long productId,
                       Long reviewerId,
                       Long revieweeId,
                       SaleReviewType saleReviewType,
                       String content) {

        checkArgument(productId != null, "productId 값은 필수입니다.");
        checkArgument(reviewerId != null, "reviewerId 값은 필수입니다.");
        checkArgument(revieweeId != null, "revieweeId 값은 필수입니다.");
        checkArgument(isNotEmpty(content), "content 값은 필수입니다.");

        Product product = productService.getProduct(productId);
        User reviewer = userService.getUser(reviewerId);

        if (reviewer.isEmptyLocation() || isEmpty(reviewer.getNickname())) {
            throw new IllegalStateException("리뷰를 남기는 사용자는 주소와 닉네임이 필수입니다.");
        }

        User reviewee = userService.getUser(revieweeId);

        if (!isValidCreateRequest(product, reviewer, reviewee, saleReviewType)) {
            throw new UnauthorizedException("올바른 요청이 아닙니다. 실제 판매자와 구매자만 리뷰를 남길 수 있습니다.");
        }

        SaleReview saleReview = SaleReview.builder()
                                        .product(product)
                                        .reviewer(reviewer)
                                        .reviewee(reviewee)
                                        .saleReviewType(saleReviewType)
                                        .content(content)
                                        .build();

        return saleReviewRepository.save(saleReview).getId();
    }

    @Override
    public boolean isValidCreateRequest(Product product,
                                        User reviewer,
                                        User reviewee,
                                        SaleReviewType saleReviewTypeCode) {

        if (saleReviewTypeCode.equals(SaleReviewType.SELLER_REVIEW)) {
            return product.isSeller(reviewer.getId()) && product.isBuyer(reviewee.getId());
        }

        if (saleReviewTypeCode.equals(SaleReviewType.BUYER_REVIEW)) {
            return product.isBuyer(reviewer.getId()) && product.isSeller(reviewee.getId());
        }

        return false;
    }

    @Override
    public SaleReview getSaleReview(Long id) {
        checkArgument(id != null, "id 값은 필수입니다.");

        return saleReviewRepository.findBySaleReviewId(id)
                .orElseThrow(() -> new NotFoundException(SaleReview.class, String.format("saleReviewId = %s", id)));
    }

    @Override
    public List<SaleReview> getUserReviews(Long userId, Pageable pageable) {
        checkArgument(userId != null, "userId 값은 필수입니다.");

        return saleReviewRepository.findAllUserReview(userId, pageable);
    }

    @Override
    public List<SaleReview> getSellerReviews(Long userId, Pageable pageable) {
        checkArgument(userId != null, "userId 값은 필수입니다.");

        return saleReviewRepository.findAllSellerReview(userId, pageable);
    }

    @Override
    public List<SaleReview> getBuyerReviews(Long userId, Pageable pageable) {
        checkArgument(userId != null, "userId 값은 필수입니다.");

        return saleReviewRepository.findAllBuyerReview(userId, pageable);
    }

    @Override
    public boolean isSellerReviewWriter(Long saleReviewId, Long userId) {
        checkArgument(saleReviewId != null, "reviewId 값은 필수입니다.");
        checkArgument(userId != null, "userId 값은 필수입니다.");

        SaleReview saleReview = getSaleReview(saleReviewId);
        return saleReview.getReviewer().getId().equals(userId)
                && saleReview.getSaleReviewType().equals(SaleReviewType.SELLER_REVIEW);
    }

    @Override
    public boolean isBuyerReviewWriter(Long saleReviewId, Long userId) {
        checkArgument(saleReviewId != null, "saleReviewId 값은 필수입니다.");
        checkArgument(userId != null, "buyerId 값은 필수입니다.");

        SaleReview saleReview = getSaleReview(saleReviewId);
        return saleReview.getReviewer().getId().equals(userId)
                && saleReview.getSaleReviewType().equals(SaleReviewType.BUYER_REVIEW);
    }

    @Override
    public boolean existBuyerReview(Long productId, Long userId) {
        checkArgument(productId != null, "productId 값은 필수입니다.");
        checkArgument(userId != null, "buyerId 값은 필수입니다.");

        return saleReviewRepository.existBuyerReview(productId, userId);
    }

    @Override
    public boolean existSellerReview(Long productId, Long userId) {
        checkArgument(productId != null, "productId 값은 필수입니다.");
        checkArgument(userId != null, "sellerId 값은 필수입니다.");

        return saleReviewRepository.existSellerReview(productId, userId);
    }

    @Override
    @Transactional
    public void update(Long id, String content) {
        checkArgument(id != null, "id 값은 필수입니다.");
        checkArgument(isNotEmpty(content), "content 값은 필수입니다.");

        SaleReview updatedReview = getSaleReview(id);
        updatedReview.update(content);
    }

    @Override
    @Transactional
    public void hide(Long id, Long userId) {
        checkArgument(userId != null, "userId 값은 필수입니다.");

        SaleReview updatedReview = getSaleReview(id);
        long revieweeId = updatedReview.getReviewee().getId();

        if (userId.equals(revieweeId)) {
            updatedReview.update(SaleReviewType.HIDE);
            return;
        }

        throw new UnauthorizedException("리뷰를 받은 사람만 받은 리뷰를 숨길 수 있습니다.");
    }

    @Override
    @Transactional
    public void delete(Long id) {
        checkArgument(id != null, "id 값은 필수입니다.");

        SaleReview deletedReview = getSaleReview(id);
        saleReviewRepository.delete(deletedReview);
    }
}
