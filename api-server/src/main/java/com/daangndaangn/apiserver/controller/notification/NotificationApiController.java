package com.daangndaangn.apiserver.controller.notification;

import com.daangndaangn.apiserver.controller.notification.NotificationResponse.SimpleResponse;
import com.daangndaangn.apiserver.service.notification.NotificationService;
import com.daangndaangn.common.jwt.JwtAuthentication;
import com.daangndaangn.common.web.ApiResult;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.daangndaangn.common.web.ApiResult.OK;
import static java.util.stream.Collectors.toList;

@RequestMapping("/api/notifications")
@RestController
@RequiredArgsConstructor
public class NotificationApiController {

    private final NotificationService notificationService;

    /**
     * 안 읽은 알림 목록 조회
     *
     * GET /api/notifications
     */
    @GetMapping
    public ApiResult<List<SimpleResponse>> getNotifications(@AuthenticationPrincipal JwtAuthentication authentication,
                                                            Pageable pageable) {

        return OK(notificationService.getNotifications(authentication.getId(), pageable)
            .stream().map(SimpleResponse::from)
            .collect(toList()));
    }

    /**
     * 읽음 처리
     *
     * PUT /api/notifications/:id
     */
    @PutMapping("/{id}")
    public ApiResult<Void> toReadNotification(@PathVariable("id") Long notificationId,
                                              @AuthenticationPrincipal JwtAuthentication authentication) {

        notificationService.toRead(notificationId, authentication.getId());
        return OK(null);
    }

}
