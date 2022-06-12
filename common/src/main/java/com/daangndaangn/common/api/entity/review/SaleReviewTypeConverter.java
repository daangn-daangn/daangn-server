package com.daangndaangn.common.api.entity.review;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class SaleReviewTypeConverter implements AttributeConverter<SaleReviewType, Integer> {
    @Override
    public Integer convertToDatabaseColumn(SaleReviewType saleReviewType) {
        return saleReviewType.getCode();
    }

    @Override
    public SaleReviewType convertToEntityAttribute(Integer saleReviewTypeCode) {
        return SaleReviewType.from(saleReviewTypeCode);
    }
}
