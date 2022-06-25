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
    private final Long saleReviewId;
    private final NotificationType notificationType;

    public static BuyerReviewCreatedEvent from(SaleReview saleReview) {
        return new BuyerReviewCreatedEvent(saleReview.getReviewee().getId(), saleReview.getId());
    }

    private BuyerReviewCreatedEvent(Long sellerId, Long saleReviewId) {
        this.sellerId = sellerId;
        this.saleReviewId = saleReviewId;
        this.notificationType = NotificationType.BUYER_REVIEW_CREATED;
    }
}
