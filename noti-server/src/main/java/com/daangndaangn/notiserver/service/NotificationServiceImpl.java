package com.daangndaangn.notiserver.service;

import com.daangndaangn.common.api.entity.notification.Notification;
import com.daangndaangn.common.api.entity.notification.NotificationConstants;
import com.daangndaangn.common.api.entity.notification.NotificationType;
import com.daangndaangn.common.api.entity.user.User;
import com.daangndaangn.common.api.repository.notification.NotificationRepository;
import com.daangndaangn.common.api.repository.user.UserRepository;
import com.daangndaangn.common.error.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkArgument;

@Transactional
@RequiredArgsConstructor
@Service
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    @Override
    public Long createBuyerReviewCreatedNotification(Long sellerId,
                                                     NotificationType notificationType,
                                                     Long saleReviewId) {

        checkArgument(sellerId != null, "sellerId 값은 필수입니다.");
        checkArgument(notificationType != null, "notificationType 값은 필수입니다.");
        checkArgument(saleReviewId != null, "saleReviewId 값은 필수입니다.");

        User seller = getUser(sellerId);

        StringBuilder identifierBuilder = new StringBuilder();
        identifierBuilder.append(NotificationConstants.SALE_REVIEW_PREFIX).append(saleReviewId);

        Notification notification = Notification.builder()
                .user(seller)
                .notificationType(notificationType)
                .identifier(identifierBuilder.toString())
                .build();

        return notificationRepository.save(notification).getId();
    }

    @Override
    public Long createSoldOutToBuyerNotification(Long buyerId,
                                                 NotificationType notificationType,
                                                 Long sellerId,
                                                 Long productId) {

        checkArgument(buyerId != null, "buyerId 값은 필수입니다.");
        checkArgument(notificationType != null, "notificationType 값은 필수입니다.");
        checkArgument(sellerId != null, "sellerId 값은 필수입니다.");
        checkArgument(productId != null, "productId 값은 필수입니다.");

        User buyer = getUser(buyerId);

        StringBuilder identifierBuilder = new StringBuilder();
        identifierBuilder.append(NotificationConstants.PRODUCT_PREFIX).append(productId)
                         .append("-")
                         .append(NotificationConstants.SELLER_PREFIX).append(sellerId);

        Notification notification = Notification.builder()
                .user(buyer)
                .notificationType(notificationType)
                .identifier(identifierBuilder.toString())
                .build();

        return notificationRepository.save(notification).getId();
    }

    @Override
    public List<Long> createFavoriteProductNotification(List<Long> userIds,
                                                        NotificationType notificationType,
                                                        Long productId) {

        checkArgument(userIds != null, "userIds 값은 필수입니다.");
        checkArgument(notificationType != null, "notificationType 값은 필수입니다.");
        checkArgument(productId != null, "productId 값은 필수입니다.");

        return userRepository.findAll(userIds).stream().map(user -> {
            StringBuilder identifierBuilder = new StringBuilder();
            identifierBuilder.append(NotificationConstants.PRODUCT_PREFIX).append(productId);

            Notification notification = Notification.builder()
                    .user(user)
                    .notificationType(notificationType)
                    .identifier(identifierBuilder.toString())
                    .build();

            return notificationRepository.save(notification).getId();
        }).collect(Collectors.toList());
    }

    private User getUser(Long userId) {
        checkArgument(userId != null, "userId 값은 필수입니다.");
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(User.class, String.format("userId = %s", userId)));
    }
}
