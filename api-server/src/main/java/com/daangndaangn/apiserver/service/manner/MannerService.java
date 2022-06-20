package com.daangndaangn.apiserver.service.manner;

import java.util.concurrent.CompletableFuture;

public interface MannerService {

    CompletableFuture<Long> createManner(Long userId, Long evaluatorId, int score);
}
