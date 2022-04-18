package com.daangndaangn.apiserver.service.salereview;

import com.daangndaangn.apiserver.controller.salereview.SaleReviewRequest;
import com.daangndaangn.apiserver.controller.salereview.SaleReviewResponse;
import com.daangndaangn.apiserver.entity.review.SaleReview;
import com.daangndaangn.apiserver.entity.user.User;
import com.daangndaangn.apiserver.error.NotFoundException;
import com.daangndaangn.apiserver.repository.salereview.SaleReviewRepository;
import com.daangndaangn.apiserver.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
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
        return saleReviewRepository.findAllUserReview(userId, pageable).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<SaleReviewResponse.GetResponse> getAllSellerReview(Long userId, Pageable pageable) {
        return saleReviewRepository.findAllSellerReview(userId, pageable).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<SaleReviewResponse.GetResponse> getAllBuyerReview(Long userId, Pageable pageable) {
        return saleReviewRepository.findAllBuyerReview(userId, pageable).stream()
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
        Objects.requireNonNull(sellerId, "sellerId must not be null");
        Objects.requireNonNull(buyerId, "buyerId must not be null");
        Objects.requireNonNull(content, "content must not be null");

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
        Objects.requireNonNull(id, "id must not be null");
        return saleReviewRepository.findBySaleReviewId(id)
                .orElseThrow(() -> new NotFoundException(SaleReview.class, String.format("saleReviewId = %s", id)));
    }

    @Override
    public List<SaleReview> findAllUserReview(Long userId, Pageable pageable) {
        Objects.requireNonNull(userId, "userId must not be null");
        return saleReviewRepository.findAllUserReview(userId, pageable);
    }

    @Override
    public List<SaleReview> findAllSellerReview(Long sellerId, Pageable pageable) {
        Objects.requireNonNull(sellerId, "sellerId must not be null");
        return saleReviewRepository.findAllSellerReview(sellerId, pageable);
    }

    @Override
    public List<SaleReview> findAllBuyerReview(Long buyerId, Pageable pageable) {
        Objects.requireNonNull(buyerId, "buyerId must not be null");
        return saleReviewRepository.findAllBuyerReview(buyerId, pageable);
    }

    @Override
    public boolean isSellerReviewWriter(Long reviewId, Long sellerId) {
        Objects.requireNonNull(reviewId, "reviewId must not be null");
        Objects.requireNonNull(sellerId, "sellerId must not be null");

        SaleReview saleReview = findSaleReview(reviewId);
        return saleReview.getSeller().getId().equals(sellerId);
    }

    @Override
    public boolean isBuyerReviewWriter(Long reviewId, Long buyerId) {
        Objects.requireNonNull(reviewId, "reviewId must not be null");
        Objects.requireNonNull(buyerId, "buyerId must not be null");

        SaleReview saleReview = findSaleReview(reviewId);
        return saleReview.getBuyer().getId().equals(buyerId);
    }

    @Override
    @Transactional
    public SaleReview update(Long id, String content) {
        Objects.requireNonNull(id, "id must not be null");
        Objects.requireNonNull(content, "content must not be null");

        SaleReview updatedReview = findSaleReview(id);
        updatedReview.update(content);
        return updatedReview;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Objects.requireNonNull(id, "id must not be null");

        SaleReview deletedReview = findSaleReview(id);
        saleReviewRepository.delete(deletedReview);
    }

    private SaleReviewResponse.GetResponse convertToDto(SaleReview saleReview) {
        return SaleReviewResponse.GetResponse.from(saleReview);
    }
}
