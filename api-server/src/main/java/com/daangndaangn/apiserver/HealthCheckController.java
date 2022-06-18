package com.daangndaangn.apiserver;

import com.daangndaangn.common.jwt.JwtAuthentication;
import com.daangndaangn.common.web.ApiResult;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 서버가 제대로 떠있는지 확인하는 용도의 샘플 Api Controller.
 * /api/hcheck : 회원/비회원 모두 호출가능
 * /api/hcheck/with-user : 회원만 호출가능
 */
@RequestMapping("/api/health")
@RestController
public class HealthCheckController {

    @GetMapping
    public ApiResult<Long> getSystemTimeMillis() {
        return ApiResult.OK(System.currentTimeMillis());
    }

    @GetMapping("/with-user")
    public ApiResult<Long> getSystemTimeMillisWithUser(@AuthenticationPrincipal JwtAuthentication authentication) {
        return ApiResult.OK(System.currentTimeMillis());
    }
}
