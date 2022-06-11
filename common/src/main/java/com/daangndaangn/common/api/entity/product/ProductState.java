package com.daangndaangn.common.api.entity.product;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toMap;
import static org.apache.commons.lang3.ObjectUtils.isEmpty;

@Getter
@AllArgsConstructor
public enum ProductState {
    FOR_SALE(0, "판매중"),
    SOLD_OUT(1, "거래완료"),
    HIDE(2, "숨기기"),
    DELETED(3, "삭제상품"),
    REVERSED(4, "예약중");

    private static final Map<Integer, ProductState> productStateMap =
            Stream.of(values()).collect(toMap(ProductState::getCode, value -> value));

    private Integer code;
    private String state;

    public static ProductState from(int productStateCode) {
        ProductState productState = productStateMap.get(productStateCode);
        if (isEmpty(productState)) {
            //TODO: 사용자 정의 예외로 변경
            throw new IllegalArgumentException("잘못된 ProductState 타입입니다.");
        }

        return productState;
    }
}
