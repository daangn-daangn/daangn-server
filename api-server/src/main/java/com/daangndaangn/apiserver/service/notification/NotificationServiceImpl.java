package com.daangndaangn.apiserver.service.notification;

import com.daangndaangn.common.api.entity.notification.Notification;
import com.daangndaangn.common.api.entity.notification.NotificationConstants;
import com.daangndaangn.common.api.repository.notification.NotificationRepository;
import com.daangndaangn.common.error.NotFoundException;
import com.daangndaangn.common.error.UnauthorizedException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;
import static org.apache.commons.lang3.math.NumberUtils.toLong;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;

    @Override
    public List<Notification> getNotifications(Long userId, Pageable pageable) {
        return notificationRepository.findAllByUserId(userId, pageable);
    }

    @Override
    public Notification getNotification(Long id) {
        checkArgument(id != null, "id 값은 필수입니다.");

        return notificationRepository.findById(id)
            .orElseThrow(() -> new NotFoundException(Notification.class, String.format("notificationId = %s", id)));
    }

    @Override
    @Transactional
    public void toRead(Long notificationId, Long userId) {
        checkArgument(userId != null, "userId 값은 필수입니다.");

        Notification notification = getNotification(notificationId);

        if (!userId.equals(notification.getUser().getId())) {
            throw new UnauthorizedException("본인이 받은 알림만 읽음 처리할 수 있습니다.");
        }

        notification.update(true);
    }

    @Override
    public Long getProductId(String identifier) {
        checkArgument(isNotEmpty(identifier), "identifier 값은 필수입니다.");
        String productId = identifier.substring(NotificationConstants.PRODUCT_PREFIX.length());
        return toLong(productId, -1);
    }

    @Override
    public Long getProductIdOfSoldOutToBuyer(String identifier) {
        checkArgument(isNotEmpty(identifier), "identifier 값은 필수입니다.");
        String[] components = identifier.split("-");
        String productComponent = components[0];
        String productId = productComponent.substring(NotificationConstants.PRODUCT_PREFIX.length());

        return toLong(productId, -1);
    }

    @Override
    public Long getReviewerId(String identifier) {
        checkArgument(isNotEmpty(identifier), "identifier 값은 필수입니다.");
        String reviewerId = identifier.substring(NotificationConstants.SALE_REVIEW_PREFIX.length());
        return toLong(reviewerId, -1);
    }

    @Override
    @Transactional
    public void delete(Long notificationId, Long userId) {
        checkArgument(userId != null, "userId 값은 필수입니다.");

        Notification notification = getNotification(notificationId);

        if (!userId.equals(notification.getUser().getId())) {
            throw new UnauthorizedException("본인이 받은 알림만 삭제할 수 있습니다.");
        }

        notification.remove();
    }
}
