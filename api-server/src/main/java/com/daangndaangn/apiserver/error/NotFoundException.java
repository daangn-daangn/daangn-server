package com.daangndaangn.apiserver.error;

import com.daangndaangn.apiserver.util.MessageUtils;
import org.apache.commons.lang3.StringUtils;

import static org.apache.commons.lang3.ObjectUtils.isNotEmpty;

/**
 * 엔티티를 조회하지 못한 경우 내려주는 Exception
 *
 * // 사용 예시
 * new NotFoundException(User.class, "userId = 12345");
 * new NotFoundException(Product.class, "userId = 12345", 12345);
 */
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
