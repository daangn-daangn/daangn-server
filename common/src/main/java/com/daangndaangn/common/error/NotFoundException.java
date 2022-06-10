package com.daangndaangn.common.error;

import com.daangndaangn.common.util.MessageUtils;
import org.apache.commons.lang3.StringUtils;

import static org.apache.commons.lang3.ObjectUtils.isNotEmpty;

public class NotFoundException extends ServiceRuntimeException {

    static final String MESSAGE_KEY = "error.notfound";
    static final String MESSAGE_DETAILS = "error.notfound.details";

    public NotFoundException(Class<?> cls, Object... values) {
        this(cls.getSimpleName(), values);
    }

    public NotFoundException(String targetName, Object... values) {
        super(MESSAGE_KEY, MESSAGE_DETAILS,
                new String[]{targetName,
                        (isNotEmpty(values)) ? StringUtils.join(values, ",") : ""}
        );
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
