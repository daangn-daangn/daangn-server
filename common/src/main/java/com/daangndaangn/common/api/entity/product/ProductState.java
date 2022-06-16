package com.daangndaangn.common.api.entity.product;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toMap;
import static org.apache.commons.lang3.ObjectUtils.isEmpty;

/**
 * 판매중
 * 예약중
 * 거래완료
 */
@Getter
@AllArgsConstructor
public enum ProductState {
    HIDE(0, "숨기기"),
    FOR_SALE(1, "판매중"),
    SOLD_OUT(2, "거래완료"),
    REVERSED(3, "예약중"),
    DELETED(4, "삭제상품");

    private static final Map<Integer, ProductState> productStateMap =
            Stream.of(values()).collect(toMap(ProductState::getCode, value -> value));

    private Integer code;
    private String state;

    public static ProductState from(int productStateCode) {
        ProductState productState = productStateMap.get(productStateCode);
        if (isEmpty(productState)) {
            throw new IllegalArgumentException("잘못된 ProductState 타입입니다. 0~4 중 하나를 입력해야 합니다.");
        }

        return productState;
    }
}
