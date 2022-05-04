package com.daangndaangn.chatserver.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 서버가 제대로 떠있는지 확인하는 용도의 샘플 Api Controller.
 *
 * 향후 삭제 예정
 */
@RequestMapping("/chat/hcheck")
@RestController
public class HealthCheckController {

    @GetMapping
    public Long getSystemTimeMillis() {
        return System.currentTimeMillis();
    }
}
