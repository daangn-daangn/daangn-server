package com.daangndaangn.apiserver.error;

import com.daangndaangn.apiserver.util.MessageUtils;

/**
 * 권한이 없는 API를 호출할 경우 사용하는 Exception
 */
public class UnauthorizedException extends ServiceRuntimeException {

    public static final String MESSAGE_KEY = "error.auth";
    public static final String MESSAGE_DETAIL = "error.auth.details";

    public UnauthorizedException(String message) {
        super(MESSAGE_KEY, MESSAGE_DETAIL, new String[]{message});
    }

    @Override
    public String getMessage() {
        return MessageUtils.getMessage(getDetailKey(), getParams());
    }

    @Override
    public String toString() {
        return MessageUtils.getMessage(getMessageKey());
    }

}
