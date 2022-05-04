package com.daangndaangn.common.api.entity.product;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.ObjectUtils;

import java.util.Map;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toMap;

@Getter
@AllArgsConstructor
public enum ProductState {
    DELETED(1, "삭제상품"),
    REVERSED(2, "예약중"),
    FOR_SALE(3, "판매중"),
    SOLD_OUT(4, "판매완료");

    private static final Map<Integer, ProductState> productStateMap =
            Stream.of(values()).collect(toMap(ProductState::getCode, value -> value));

    private Integer code;
    private String state;

    public static ProductState from(int productStateCode) {
        ProductState productState = productStateMap.get(productStateCode);
        if (ObjectUtils.isEmpty(productState)) {
            //TODO: 사용자 정의 예외로 변경
            throw new IllegalArgumentException("잘못된 ProductState 타입");
        }

        return productState;
    }
}
