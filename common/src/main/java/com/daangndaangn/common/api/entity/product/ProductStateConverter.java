package com.daangndaangn.common.api.entity.product;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class ProductStateConverter implements AttributeConverter<ProductState, Integer> {

    @Override
    public Integer convertToDatabaseColumn(ProductState productState) {
        return productState.getCode();
    }

    @Override
    public ProductState convertToEntityAttribute(Integer productStateCode) {
        return ProductState.from(productStateCode);
    }
}
