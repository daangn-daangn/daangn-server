package com.daangndaangn.common.api.repository.user.query;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 사용자와 매너평가점수별 평가 인원 합계
 */
@Getter
@AllArgsConstructor
public class UserQueryDto {
    private Long userId;
    private int score;
    private Long scoreCount;
}
