package com.daangndaangn.common.event;

import com.daangndaangn.common.api.entity.notification.NotificationType;
import com.daangndaangn.common.api.entity.review.SaleReview;
import lombok.Getter;
import lombok.ToString;

/**
 * 구매후기를 남긴 경우
 */
@ToString
@Getter
public class BuyerReviewCreatedEvent {

    private final Long sellerId;
    private final Long reviewerId;  //Buyer
    private final NotificationType notificationType;

    public static BuyerReviewCreatedEvent from(SaleReview saleReview) {
        return new BuyerReviewCreatedEvent(saleReview.getReviewee().getId(), saleReview.getReviewer().getId());
    }

    private BuyerReviewCreatedEvent(Long sellerId, Long reviewerId) {
        this.sellerId = sellerId;
        this.reviewerId = reviewerId;
        this.notificationType = NotificationType.BUYER_REVIEW_CREATED;
    }
}
