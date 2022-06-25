package com.daangndaangn.notiserver;

import com.daangndaangn.common.web.ApiResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/noti/health")
public class HealthCheckController {
    @GetMapping
    public ApiResult<Long> getSystemTimeMillis() {
        return ApiResult.OK(System.currentTimeMillis());
    }
}
