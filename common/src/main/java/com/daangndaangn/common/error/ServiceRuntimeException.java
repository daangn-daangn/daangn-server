package com.daangndaangn.common.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public abstract class ServiceRuntimeException extends RuntimeException {

    // 발생한 예외 타입에 대한 정보만 내려줄 때 사용할 key
    private final String messageKey;

    // 발생한 예외에 대해 자세한 정보를 내려줄 때 사용할 key
    private final String detailKey;

    // 필요 시 에러 메시지에 추가할 parameter
    private final Object[] params;
}
