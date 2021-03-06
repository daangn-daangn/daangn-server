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
import com.daangndaangn.common.event.BuyerReviewCreatedEvent;
import com.google.common.eventbus.EventBus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static com.daangndaangn.common.api.entity.review.SaleReviewType.BUYER_REVIEW;
import static com.daangndaangn.common.api.entity.review.SaleReviewType.SELLER_REVIEW;
import static com.google.common.base.Preconditions.checkArgument;
import static java.util.concurrent.CompletableFuture.completedFuture;
import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class SaleReviewServiceImpl implements SaleReviewService {

    private final SaleReviewRepository saleReviewRepository;
    private final ProductService productService;
    private final UserService userService;
    private final EventBus eventBus;

    @Async
    @Override
    @Transactional
    public CompletableFuture<Long> create(Long productId,
                                          Long reviewerId,
                                          Long revieweeId,
                                          SaleReviewType saleReviewType,
                                          String content) {

        checkArgument(productId != null, "productId ?????? ???????????????.");
        checkArgument(reviewerId != null, "reviewerId ?????? ???????????????.");
        checkArgument(revieweeId != null, "revieweeId ?????? ???????????????.");
        checkArgument(isNotEmpty(content), "content ?????? ???????????????.");

        Product product = productService.getProduct(productId);
        User reviewer = userService.getUser(reviewerId);

        if (reviewer.isEmptyLocation() || isEmpty(reviewer.getNickname())) {
            throw new IllegalStateException("????????? ????????? ???????????? ????????? ???????????? ???????????????.");
        }

        User reviewee = userService.getUser(revieweeId);

        if (!isValidCreateRequest(product, reviewer, reviewee, saleReviewType)) {
            throw new UnauthorizedException("????????? ????????? ????????????. ?????? ???????????? ???????????? ????????? ?????? ??? ????????????.");
        }

        SaleReview saleReview = SaleReview.builder()
                                        .product(product)
                                        .reviewer(reviewer)
                                        .reviewee(reviewee)
                                        .saleReviewType(saleReviewType)
                                        .content(content)
                                        .build();

        SaleReview savedSaleReview = saleReviewRepository.save(saleReview);

        //????????? ????????? db??? ????????? ??????
        if (saleReviewType.equals(BUYER_REVIEW) && savedSaleReview.getId() != null) {
            eventBus.post(BuyerReviewCreatedEvent.from(savedSaleReview));
        }

        return completedFuture(savedSaleReview.getId());
    }

    @Override
    public boolean isValidCreateRequest(Product product,
                                        User reviewer,
                                        User reviewee,
                                        SaleReviewType saleReviewTypeCode) {

        if (saleReviewTypeCode.equals(SELLER_REVIEW)) {
            return product.isSeller(reviewer.getId()) && product.isBuyer(reviewee.getId());
        }

        if (saleReviewTypeCode.equals(BUYER_REVIEW)) {
            return product.isBuyer(reviewer.getId()) && product.isSeller(reviewee.getId());
        }

        return false;
    }

    @Override
    public SaleReview getSaleReview(Long id) {
        checkArgument(id != null, "id ?????? ???????????????.");

        return saleReviewRepository.findBySaleReviewId(id)
                .orElseThrow(() -> new NotFoundException(SaleReview.class, String.format("saleReviewId = %s", id)));
    }

    @Override
    public List<SaleReview> getUserReviews(Long userId, Pageable pageable) {
        checkArgument(userId != null, "userId ?????? ???????????????.");

        return saleReviewRepository.findAllUserReview(userId, pageable);
    }

    @Override
    public List<SaleReview> getSellerReviews(Long userId, Pageable pageable) {
        checkArgument(userId != null, "userId ?????? ???????????????.");

        return saleReviewRepository.findAllSellerReview(userId, pageable);
    }

    @Override
    public List<SaleReview> getBuyerReviews(Long userId, Pageable pageable) {
        checkArgument(userId != null, "userId ?????? ???????????????.");

        return saleReviewRepository.findAllBuyerReview(userId, pageable);
    }

    @Override
    public boolean isSellerReviewWriter(Long saleReviewId, Long userId) {
        checkArgument(saleReviewId != null, "reviewId ?????? ???????????????.");
        checkArgument(userId != null, "userId ?????? ???????????????.");

        SaleReview saleReview = getSaleReview(saleReviewId);
        return saleReview.getReviewer().getId().equals(userId)
                && saleReview.getSaleReviewType().equals(SELLER_REVIEW);
    }

    @Override
    public boolean isBuyerReviewWriter(Long saleReviewId, Long userId) {
        checkArgument(saleReviewId != null, "saleReviewId ?????? ???????????????.");
        checkArgument(userId != null, "buyerId ?????? ???????????????.");

        SaleReview saleReview = getSaleReview(saleReviewId);
        return saleReview.getReviewer().getId().equals(userId)
                && saleReview.getSaleReviewType().equals(BUYER_REVIEW);
    }

    @Override
    public boolean existBuyerReview(Long productId, Long userId) {
        checkArgument(productId != null, "productId ?????? ???????????????.");
        checkArgument(userId != null, "buyerId ?????? ???????????????.");

        return saleReviewRepository.existBuyerReview(productId, userId);
    }

    @Override
    public boolean existSellerReview(Long productId, Long userId) {
        checkArgument(productId != null, "productId ?????? ???????????????.");
        checkArgument(userId != null, "sellerId ?????? ???????????????.");

        return saleReviewRepository.existSellerReview(productId, userId);
    }

    @Override
    @Transactional
    public void update(Long id, String content) {
        checkArgument(id != null, "id ?????? ???????????????.");
        checkArgument(isNotEmpty(content), "content ?????? ???????????????.");

        SaleReview updatedReview = getSaleReview(id);
        updatedReview.update(content);
    }

    @Override
    @Transactional
    public void hide(Long id, Long userId) {
        checkArgument(userId != null, "userId ?????? ???????????????.");

        SaleReview updatedReview = getSaleReview(id);
        long revieweeId = updatedReview.getReviewee().getId();

        if (userId.equals(revieweeId)) {
            updatedReview.update(SaleReviewType.HIDE);
            return;
        }

        throw new UnauthorizedException("????????? ?????? ????????? ?????? ????????? ?????? ??? ????????????.");
    }

    @Override
    @Transactional
    public void delete(Long id) {
        checkArgument(id != null, "id ?????? ???????????????.");

        SaleReview deletedReview = getSaleReview(id);
        saleReviewRepository.delete(deletedReview);
    }
}
