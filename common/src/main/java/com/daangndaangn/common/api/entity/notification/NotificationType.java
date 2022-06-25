package com.daangndaangn.common.api.entity.notification;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toMap;
import static org.apache.commons.lang3.ObjectUtils.isEmpty;

@Getter
@AllArgsConstructor
public enum NotificationType {
    SOLD_OUT(0,"찜한 상품이 거래 완료된 경우"),
    PRICE_DOWN(1,"찜한 상품의 가격이 낮아진 경우"),
    SOLD_OUT_TO_BUYER(2,"판매자가 판매완료로 상태를 변경한 경우"),
    BUYER_REVIEW_CREATED(3,"구매후기를 남긴 경우");

    private static final Map<Integer, NotificationType> notiTypeMap =
            Stream.of(values()).collect(toMap(NotificationType::getCode, value -> value));

    private Integer code;
    private String state;

    public static NotificationType from(int notiTypeCode) {
        NotificationType notiType = notiTypeMap.get(notiTypeCode);
        if (isEmpty(notiType)) {
            throw new IllegalArgumentException("잘못된 NotificationType 타입입니다. 0~3 중 하나를 입력해야 합니다.");
        }

        return notiType;
    }
}
