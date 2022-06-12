package com.daangndaangn.common.api.entity.review;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toMap;
import static org.apache.commons.lang3.ObjectUtils.isEmpty;

@Getter
@AllArgsConstructor
public enum SaleReviewType {
    SELLER_REVIEW(0, "판매자 후기"),
    BUYER_REVIEW(1, "구매자 후기");

    private static final Map<Integer, SaleReviewType> saleReviewTypeMap =
            Stream.of(values()).collect(toMap(SaleReviewType::getCode, value -> value));

    private Integer code;
    private String state;

    public static SaleReviewType from(int saleReviewTypeCode) {
        SaleReviewType saleReviewType = saleReviewTypeMap.get(saleReviewTypeCode);
        if (isEmpty(saleReviewType)) {
            //TODO: 사용자 정의 예외로 변경
            throw new IllegalArgumentException("잘못된 SaleReviewType 타입입니다.");
        }

        return saleReviewType;
    }
}
