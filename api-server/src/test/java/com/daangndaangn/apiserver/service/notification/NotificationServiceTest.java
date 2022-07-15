package com.daangndaangn.apiserver.service.notification;

import com.daangndaangn.common.api.entity.notification.Notification;
import com.daangndaangn.common.api.entity.notification.NotificationConstants;
import com.daangndaangn.common.api.entity.notification.NotificationType;
import com.daangndaangn.common.api.entity.user.User;
import com.daangndaangn.common.api.repository.notification.NotificationRepository;
import com.daangndaangn.common.error.UnauthorizedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

    @InjectMocks
    private NotificationServiceImpl notificationService;

    @Mock
    private NotificationRepository notificationRepository;

    private User mockUser;
    private Notification mockNotification;

    @BeforeEach
    void init() {
        mockUser = User.builder().id(1L).oauthId(12345L).profileUrl("").build();
        mockNotification = Notification.builder().id(1L)
                .user(mockUser)
                .notificationType(NotificationType.SOLD_OUT)
                .identifier("test")
                .build();
    }

    @Test
    @DisplayName("자신이_받은_알림을_읽음_상태로_변경할_수_있다")
    void toRead() {
        //given
        given(notificationRepository.findById(anyLong())).willReturn(Optional.ofNullable(mockNotification));

        boolean before = mockNotification.isRead();

        //when
        notificationService.toRead(mockNotification.getId(), mockUser.getId());

        boolean after = mockNotification.isRead();

        //then
        assertThat(before).isEqualTo(false);
        assertThat(after).isEqualTo(true);
        verify(notificationRepository).findById(anyLong());
    }

    @Test
    @DisplayName("자신이_받지않은_알림을_읽음_상태로_변경하면_예외를_반환한다")
    void toRead2() {
        //given
        given(notificationRepository.findById(anyLong())).willReturn(Optional.ofNullable(mockNotification));

        Long invalidUserId = 999L;

        //when
        assertThatThrownBy(() -> notificationService.toRead(mockNotification.getId(), invalidUserId))
            .isInstanceOf(UnauthorizedException.class);

        //then
        verify(notificationRepository).findById(anyLong());
    }

    @Test
    @DisplayName("SOLD_OUT과_PRICE_DOWN의_identifier에서_product_id를_추출할_수_있다")
    public void getProductId() {
        //given
        Long productId = 123L;
        String identifier = NotificationConstants.PRODUCT_PREFIX + productId;

        //when
        Long result = notificationService.getProductId(identifier);

        //then
        assertThat(result).isEqualTo(productId);
    }

    @Test
    @DisplayName("SOLD_OUT_TO_BUYER의_identifier에서_product_id를_추출할_수_있다")
    public void getProductIdOfSoldOutToBuyer() {
        //given
        Long productId = 123L;
        Long sellerId = 456L;
        StringBuilder sb = new StringBuilder();
        String identifier =
                sb.append(NotificationConstants.PRODUCT_PREFIX).append(productId)
                      .append("-")
                  .append(NotificationConstants.SELLER_PREFIX).append(sellerId).toString();

        //when
        Long result = notificationService.getProductIdOfSoldOutToBuyer(identifier);

        //then
        assertThat(result).isEqualTo(productId);
    }

    @Test
    @DisplayName("BUYER_REVIEW_CREATED의_identifier에서_product_id를_추출할_수_있다")
    public void getReviewerId() {
        //given
        Long reviewerId = 123L;
        String identifier = NotificationConstants.SALE_REVIEW_PREFIX + reviewerId;

        //when
        Long result = notificationService.getReviewerId(identifier);

        //then
        assertThat(result).isEqualTo(reviewerId);
    }

    @Test
    @DisplayName("잘못된_identifier인_경우_minus_1을_반환한다")
    public void getProductId_getProductIdOfSoldOutToBuyer_getReviewerId() {
        //given
        Long productId = 123L;
        String invalidIdentifier = NotificationConstants.PRODUCT_PREFIX + productId + "invalid";

        //when
        Long result1 = notificationService.getProductId(invalidIdentifier);
        Long result2 = notificationService.getProductIdOfSoldOutToBuyer(invalidIdentifier);
        Long result3 = notificationService.getReviewerId(invalidIdentifier);

        //then
        assertThat(result1).isEqualTo(-1);
        assertThat(result2).isEqualTo(-1);
        assertThat(result3).isEqualTo(-1);
    }

    @Test
    @DisplayName("자신이_받은_알림을_삭제할_수_있다")
    void delete1() {
        //given
        given(notificationRepository.findById(anyLong())).willReturn(Optional.ofNullable(mockNotification));

        boolean before = mockNotification.isValid();

        //when
        notificationService.delete(mockNotification.getId(), mockUser.getId());

        boolean after = mockNotification.isValid();

        //then
        assertThat(before).isEqualTo(true);
        assertThat(after).isEqualTo(false);
        verify(notificationRepository).findById(anyLong());
    }

    @Test
    @DisplayName("자신이_받지않은_알림을_삭제하면_예외를_반환한다")
    void delete2() {
        //given
        given(notificationRepository.findById(anyLong())).willReturn(Optional.ofNullable(mockNotification));

        Long invalidUserId = 999L;

        //when
        assertThatThrownBy(() -> notificationService.delete(mockNotification.getId(), invalidUserId))
                .isInstanceOf(UnauthorizedException.class);

        //then
        verify(notificationRepository).findById(anyLong());
    }
}