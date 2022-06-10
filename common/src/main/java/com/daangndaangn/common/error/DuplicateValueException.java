package com.daangndaangn.common.error;

import com.daangndaangn.common.util.MessageUtils;

/**
 * 중복된 값을 넣으려고 한 경우 내려주는 Exception
 */
public class DuplicateValueException extends ServiceRuntimeException {

    public static final String MESSAGE_KEY = "error.duplicate";
    public static final String MESSAGE_DETAIL = "error.duplicate.details";

    public DuplicateValueException(String message) {
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
