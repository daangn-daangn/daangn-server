package com.daangndaangn.apiserver.service.salereview;

import com.daangndaangn.common.api.entity.review.SaleReview;
import com.daangndaangn.common.api.entity.user.User;
import com.daangndaangn.common.api.repository.salereview.SaleReviewRepository;
import com.daangndaangn.apiserver.service.user.UserService;
import com.daangndaangn.common.error.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;


@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class SaleReviewServiceImpl implements SaleReviewService {

    private final UserService userService;
    private final SaleReviewRepository saleReviewRepository;

    @Override
    @Transactional
    public Long create(Long sellerId, Long buyerId, String content) {
        checkArgument(sellerId != null, "sellerId 값은 필수입니다.");
        checkArgument(buyerId != null, "buyerId 값은 필수입니다.");
        checkArgument(StringUtils.isNotEmpty(content), "content 값은 필수입니다.");

        User seller = userService.getUser(sellerId);
        User buyer = userService.getUser(buyerId);

        SaleReview saleReview = SaleReview.builder()
                                        .seller(seller)
                                        .buyer(buyer)
                                        .content(content)
                                        .build();

        return saleReviewRepository.save(saleReview).getId();
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
    public List<SaleReview> getSellerReviews(Long sellerId, Pageable pageable) {
        checkArgument(sellerId != null, "sellerId 값은 필수입니다.");

        return saleReviewRepository.findAllSellerReview(sellerId, pageable);
    }

    @Override
    public List<SaleReview> getBuyerReviews(Long buyerId, Pageable pageable) {
        checkArgument(buyerId != null, "buyerId 값은 필수입니다.");

        return saleReviewRepository.findAllBuyerReview(buyerId, pageable);
    }

    @Override
    public boolean isSellerReviewWriter(Long reviewId, Long sellerId) {
        checkArgument(reviewId != null, "reviewId 값은 필수입니다.");
        checkArgument(sellerId != null, "sellerId 값은 필수입니다.");

        SaleReview saleReview = getSaleReview(reviewId);
        return saleReview.getSeller().getId().equals(sellerId);
    }

    @Override
    public boolean isBuyerReviewWriter(Long reviewId, Long buyerId) {
        checkArgument(reviewId != null, "reviewId 값은 필수입니다.");
        checkArgument(buyerId != null, "buyerId 값은 필수입니다.");

        SaleReview saleReview = getSaleReview(reviewId);
        return saleReview.getBuyer().getId().equals(buyerId);
    }

    @Override
    @Transactional
    public void update(Long id, String content) {
        checkArgument(id != null, "id 값은 필수입니다.");
        checkArgument(StringUtils.isNotEmpty(content), "content 값은 필수입니다.");

        SaleReview updatedReview = getSaleReview(id);
        updatedReview.update(content);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        checkArgument(id != null, "id 값은 필수입니다.");

        SaleReview deletedReview = getSaleReview(id);
        saleReviewRepository.delete(deletedReview);
    }
}
