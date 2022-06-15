package com.daangndaangn.chatserver;

import com.daangndaangn.common.jwt.JwtAuthentication;
import com.daangndaangn.common.web.ApiResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 서버가 제대로 떠있는지 확인하는 용도의 샘플 Api Controller.
 */
@Slf4j
@RequestMapping("/chat/health")
@RestController
public class HealthCheckController {

    @GetMapping
    public ApiResult<Long> getSystemTimeMillis() {
        return ApiResult.OK(System.currentTimeMillis());
    }

    @GetMapping("/with-user")
    public ApiResult<Long> getSystemTimeMillisWithUser(@AuthenticationPrincipal JwtAuthentication authentication) {
        log.info("authentication.getId(): {}", authentication.getId());
        log.info("authentication.getOauthId(): {}", authentication.getOauthId());
        log.info("authentication.getNickname(): {}", authentication.getNickname());
        log.info("authentication.getLocation(): {}", authentication.getLocation());

        return ApiResult.OK(System.currentTimeMillis());
    }
}
