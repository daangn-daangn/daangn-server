package com.daangndaangn.apiserver.service.salereview;

import com.daangndaangn.apiserver.controller.salereview.SaleReviewRequest;
import com.daangndaangn.apiserver.controller.salereview.SaleReviewResponse;
import com.daangndaangn.apiserver.entity.review.SaleReview;
import com.daangndaangn.apiserver.entity.user.User;
import com.daangndaangn.apiserver.error.NotFoundException;
import com.daangndaangn.apiserver.repository.salereview.SaleReviewRepository;
import com.daangndaangn.apiserver.service.user.UserService;
import com.google.common.base.Preconditions;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class SaleReviewServiceImpl implements SaleReviewService {

    private final UserService userService;
    private final SaleReviewRepository saleReviewRepository;

    @Override
    @Transactional
    public Long createSaleReview(SaleReviewRequest.CreateRequest request) {
        SaleReview saleReview = create(request.getSellerId(), request.getBuyerId(), request.getContent());
        return saleReview.getId();
    }

    @Override
    public SaleReviewResponse.GetResponse getSaleReview(Long id) {
        return convertToDto(findSaleReview(id));
    }

    @Override
    public List<SaleReviewResponse.GetResponse> getAllUserReview(Long userId, Pageable pageable) {
        return findAllUserReview(userId, pageable).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<SaleReviewResponse.GetResponse> getAllSellerReview(Long userId, Pageable pageable) {
        return findAllSellerReview(userId, pageable).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<SaleReviewResponse.GetResponse> getAllBuyerReview(Long userId, Pageable pageable) {
        return findAllBuyerReview(userId, pageable).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void updateSaleReview(Long id, SaleReviewRequest.UpdateRequest request) {
        update(id, request.getContent());
    }

    @Override
    @Transactional
    public SaleReview create(Long sellerId, Long buyerId, String content) {
        Preconditions.checkArgument(ObjectUtils.isNotEmpty(sellerId), "sellerId 값은 필수입니다.");
        Preconditions.checkArgument(ObjectUtils.isNotEmpty(buyerId), "buyerId 값은 필수입니다.");
        Preconditions.checkArgument(StringUtils.isNotEmpty(content), "content 값은 필수입니다.");

        User seller = userService.findUser(sellerId);
        User buyer = userService.findUser(buyerId);

        SaleReview saleReview = SaleReview.builder()
                                        .seller(seller)
                                        .buyer(buyer)
                                        .content(content)
                                        .build();

        return saleReviewRepository.save(saleReview);
    }

    @Override
    public SaleReview findSaleReview(Long id) {
        Preconditions.checkArgument(ObjectUtils.isNotEmpty(id), "id 값은 필수입니다.");

        return saleReviewRepository.findBySaleReviewId(id)
                .orElseThrow(() -> new NotFoundException(SaleReview.class, String.format("saleReviewId = %s", id)));
    }

    @Override
    public List<SaleReview> findAllUserReview(Long userId, Pageable pageable) {
        Preconditions.checkArgument(ObjectUtils.isNotEmpty(userId), "userId 값은 필수입니다.");

        return saleReviewRepository.findAllUserReview(userId, pageable);
    }

    @Override
    public List<SaleReview> findAllSellerReview(Long sellerId, Pageable pageable) {
        Preconditions.checkArgument(ObjectUtils.isNotEmpty(sellerId), "sellerId 값은 필수입니다.");

        return saleReviewRepository.findAllSellerReview(sellerId, pageable);
    }

    @Override
    public List<SaleReview> findAllBuyerReview(Long buyerId, Pageable pageable) {
        Preconditions.checkArgument(ObjectUtils.isNotEmpty(buyerId), "buyerId 값은 필수입니다.");

        return saleReviewRepository.findAllBuyerReview(buyerId, pageable);
    }

    @Override
    public boolean isSellerReviewWriter(Long reviewId, Long sellerId) {
        Preconditions.checkArgument(ObjectUtils.isNotEmpty(reviewId), "reviewId 값은 필수입니다.");
        Preconditions.checkArgument(ObjectUtils.isNotEmpty(sellerId), "sellerId 값은 필수입니다.");

        SaleReview saleReview = findSaleReview(reviewId);
        return saleReview.getSeller().getId().equals(sellerId);
    }

    @Override
    public boolean isBuyerReviewWriter(Long reviewId, Long buyerId) {
        Preconditions.checkArgument(ObjectUtils.isNotEmpty(reviewId), "reviewId 값은 필수입니다.");
        Preconditions.checkArgument(ObjectUtils.isNotEmpty(buyerId), "buyerId 값은 필수입니다.");

        SaleReview saleReview = findSaleReview(reviewId);
        return saleReview.getBuyer().getId().equals(buyerId);
    }

    @Override
    @Transactional
    public SaleReview update(Long id, String content) {
        Preconditions.checkArgument(ObjectUtils.isNotEmpty(id), "id 값은 필수입니다.");
        Preconditions.checkArgument(StringUtils.isNotEmpty(content), "content 값은 필수입니다.");

        SaleReview updatedReview = findSaleReview(id);
        updatedReview.update(content);
        return updatedReview;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Preconditions.checkArgument(ObjectUtils.isNotEmpty(id), "id 값은 필수입니다.");

        SaleReview deletedReview = findSaleReview(id);
        saleReviewRepository.delete(deletedReview);
    }

    private SaleReviewResponse.GetResponse convertToDto(SaleReview saleReview) {
        return SaleReviewResponse.GetResponse.from(saleReview);
    }
}
