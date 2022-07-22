package com.daangndaangn.notiserver.service;

import com.daangndaangn.common.api.entity.notification.Notification;
import com.daangndaangn.common.api.entity.user.User;
import com.daangndaangn.common.api.repository.notification.NotificationRepository;
import com.daangndaangn.common.api.repository.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static com.daangndaangn.common.api.entity.notification.NotificationType.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

    @InjectMocks
    private NotificationServiceImpl notificationService;

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private UserRepository userRepository;

    private User mockUser;

    @BeforeEach
    void init() {
        mockUser = User.builder().id(1L).oauthId(12345L).build();
    }

    @Test
    @DisplayName("구매자가_구매후기를_남긴_경우_알림을_만들_수_있다")
    void createBuyerReviewCreatedNotification() {
        //given
        Notification mockNotification = Notification.builder()
                .id(1L)
                .user(mockUser)
                .notificationType(BUYER_REVIEW_CREATED)
                .identifier("test-identifier")
                .build();

        given(userRepository.findById(anyLong())).willReturn(Optional.ofNullable(mockUser));
        given(notificationRepository.save(any())).willReturn(mockNotification);
        Long saleReviewId = 1L;

        //when
        Long notificationId = notificationService.createBuyerReviewCreatedNotification(mockUser.getId(),
                                                                                        BUYER_REVIEW_CREATED,
                                                                                        saleReviewId);

        //then
        verify(userRepository).findById(anyLong());
        verify(notificationRepository).save(any());
        assertThat(notificationId).isEqualTo(mockNotification.getId());
    }

    @Test
    @DisplayName("판매자가_판매완료로_상태를_변경한_경우_알림을_만들_수_있다")
    void createSoldOutToBuyerNotification() {
        //given
        Notification mockNotification = Notification.builder()
                .id(1L)
                .user(mockUser)
                .notificationType(SOLD_OUT_TO_BUYER)
                .identifier("test-identifier")
                .build();

        given(userRepository.findById(anyLong())).willReturn(Optional.ofNullable(mockUser));
        given(notificationRepository.save(any())).willReturn(mockNotification);
        Long sellerId = 1L;
        Long productId = 1L;

        //when
        Long notificationId = notificationService.createSoldOutToBuyerNotification(mockUser.getId(),
                                                                                    BUYER_REVIEW_CREATED,
                                                                                    sellerId, productId);

        //then
        verify(userRepository).findById(anyLong());
        verify(notificationRepository).save(any());
        assertThat(notificationId).isEqualTo(mockNotification.getId());
    }

    @Test
    @DisplayName("찜한_상품에_대한_이벤트가_일어난_경우_관계된_사람들의_수_만큼_알림을_만들_수_있다")
    void createFavoriteProductNotification() {
        //given
        Notification mockNotification = Notification.builder()
                .id(1L)
                .user(mockUser)
                .notificationType(SOLD_OUT)
                .identifier("test-identifier")
                .build();

        List<Long> userIds = List.of(mockUser.getId(), mockUser.getId(), mockUser.getId());
        List<User> mockUsers = List.of(mockUser, mockUser, mockUser, mockUser);

        given(userRepository.findAll(anyList())).willReturn(mockUsers);
        given(notificationRepository.save(any())).willReturn(mockNotification);
        Long productId = 1L;

        //when
        List<Long> notificationIds = notificationService.createFavoriteProductNotification(userIds,
                SOLD_OUT, productId);

        //then
        verify(userRepository).findAll(anyList());
        verify(notificationRepository, times(mockUsers.size())).save(any());
        assertThat(notificationIds.size()).isEqualTo(mockUsers.size());
    }
}