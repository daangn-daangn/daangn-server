package com.daangndaangn.apiserver.controller.notification;

import com.daangndaangn.apiserver.controller.notification.NotificationResponse.SimpleResponse;
import com.daangndaangn.apiserver.service.notification.NotificationService;
import com.daangndaangn.apiserver.service.notification.query.NotificationQueryService;
import com.daangndaangn.common.jwt.JwtAuthentication;
import com.daangndaangn.common.util.PresignerUtils;
import com.daangndaangn.common.controller.ApiResult;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.daangndaangn.common.api.entity.notification.NotificationType.BUYER_REVIEW_CREATED;
import static com.daangndaangn.common.api.entity.notification.NotificationType.SOLD_OUT_TO_BUYER;
import static com.daangndaangn.common.controller.ApiResult.OK;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.StringUtils.isEmpty;

@RequestMapping("/api/notifications")
@RestController
@RequiredArgsConstructor
public class NotificationApiController {

    private final NotificationService notificationService;
    private final NotificationQueryService notificationQueryService;
    private final PresignerUtils presignerUtils;

    /**
     * 안 읽은 알림 목록 조회
     *
     * GET /api/notifications
     */
    @GetMapping
    public ApiResult<List<SimpleResponse>> getNotifications(@AuthenticationPrincipal JwtAuthentication authentication,
                                                            Pageable pageable) {

        return OK(notificationQueryService.getNotifications(authentication.getId(), pageable)
            .stream().map(notiDto -> {
                if (notiDto.getNotiType().equals(BUYER_REVIEW_CREATED)) {
                    String presignedUrl = isEmpty(notiDto.getProfileUrl()) ? null :
                            presignerUtils.getProfilePresignedGetUrl(notiDto.getProfileUrl());
                    return SimpleResponse.fromUserNotice(notiDto, presignedUrl);
                } else if (notiDto.getNotiType().equals(SOLD_OUT_TO_BUYER)) {
                    String productPresignedUrl = isEmpty(notiDto.getThumbNailImage()) ? null :
                            presignerUtils.getProductPresignedGetUrl(notiDto.getThumbNailImage());
                    String profilePresignedUrl = isEmpty(notiDto.getProfileUrl()) ? null :
                            presignerUtils.getProfilePresignedGetUrl(notiDto.getProfileUrl());
                    return SimpleResponse.fromProductAndUserNotice(notiDto, productPresignedUrl, profilePresignedUrl);
                } else {
                    String presignedUrl = isEmpty(notiDto.getThumbNailImage()) ? null :
                            presignerUtils.getProductPresignedGetUrl(notiDto.getThumbNailImage());
                    return SimpleResponse.fromProductNotice(notiDto, presignedUrl);
                }
            })
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
        return OK();
    }

    /**
     * 삭제 처리
     *
     * DELETE /api/notifications/:id
     */
    @DeleteMapping("/{id}")
    public ApiResult<Void> deleteNotifications(@PathVariable("id") Long notificationId,
                                              @AuthenticationPrincipal JwtAuthentication authentication) {

        notificationService.delete(notificationId, authentication.getId());
        return OK();
    }
}
