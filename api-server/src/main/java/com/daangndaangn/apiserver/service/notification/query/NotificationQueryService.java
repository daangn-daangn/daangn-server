package com.daangndaangn.apiserver.service.notification.query;

import com.daangndaangn.apiserver.service.notification.NotificationService;
import com.daangndaangn.apiserver.service.product.ProductService;
import com.daangndaangn.apiserver.service.user.UserService;
import com.daangndaangn.common.api.entity.product.Product;
import com.daangndaangn.common.api.entity.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class NotificationQueryService {

    private final NotificationService notificationService;
    private final ProductService productService;
    private final UserService userService;

    public List<NotificationQueryDto> getNotifications(Long userId, Pageable pageable) {
        return notificationService.getNotifications(userId, pageable).stream().map(notification -> {
            switch (notification.getNotificationType()) {
                case SOLD_OUT:
                    Long productId = notificationService.getProductId(notification.getIdentifier());
                    Product product = productService.getProduct(productId);
                    return NotificationQueryDto.from(product, notification);
                case PRICE_DOWN:
                    productId = notificationService.getProductId(notification.getIdentifier());
                    product = productService.getProduct(productId);
                    return NotificationQueryDto.from(product, notification);
                case SOLD_OUT_TO_BUYER:
                    productId = notificationService.getProductIdOfSoldOutToBuyer(notification.getIdentifier());
                    Long sellerId = notificationService.getSellerIdOfSoldOutToBuyer(notification.getIdentifier());
                    product = productService.getProduct(productId);
                    User seller = userService.getUser(sellerId);
                    return NotificationQueryDto.of(product, seller, notification);
                default:
                    //BUYER_REVIEW_CREATED
                    Long reviewerId = notificationService.getReviewerId(notification.getIdentifier());
                    User reviewer = userService.getUser(reviewerId);
                    return NotificationQueryDto.from(reviewer, notification);
            }
        }).collect(toList());
    }

}
