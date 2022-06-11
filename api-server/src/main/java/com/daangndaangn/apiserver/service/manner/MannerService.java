package com.daangndaangn.apiserver.service.manner;

public interface MannerService {

    Long createManner(Long userId, Long evaluatorId, int score);
}
