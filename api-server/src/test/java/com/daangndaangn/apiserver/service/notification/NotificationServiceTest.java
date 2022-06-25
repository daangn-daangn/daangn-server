package com.daangndaangn.apiserver.service.notification;

import com.daangndaangn.common.api.entity.notification.Notification;
import com.daangndaangn.common.api.entity.notification.NotificationType;
import com.daangndaangn.common.api.entity.user.User;
import com.daangndaangn.common.api.repository.notification.NotificationRepository;
import com.daangndaangn.common.error.UnauthorizedException;
import org.junit.jupiter.api.BeforeEach;
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
    void 자신이_받은_알림을_읽음_상태로_변경할_수_있다() {
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
    void 자신이_받지않은_알림을_읽음_상태로_변경하면_예외를_반환한다() {
        //given
        given(notificationRepository.findById(anyLong())).willReturn(Optional.ofNullable(mockNotification));

        boolean before = mockNotification.isRead();
        Long invalidUserId = 999L;

        //when
        assertThatThrownBy(() -> notificationService.toRead(mockNotification.getId(), invalidUserId))
            .isInstanceOf(UnauthorizedException.class);

        //then
        verify(notificationRepository).findById(anyLong());
    }
}