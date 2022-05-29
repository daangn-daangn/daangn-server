package com.daangndaangn.common.api.repository.product.query;

import lombok.Builder;
import lombok.Getter;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.StringUtils.isEmpty;

/**
 * ProductQueryRepository에서 ProductQueryDto 조회 시 사용하는 옵션
 */
@Builder
@Getter
public class ProductSearchOption {
    List<Long> categories;
    private String title;

    public static ProductSearchOption of(String title, String category) {
        if (isEmpty(category)) {
            return ProductSearchOption.builder()
                    .title(title)
                    .categories(Collections.emptyList())
                    .build();
        } else {
            /**
             * <예시>
             * queryString = ?title=맥북&category=1,2,3,4,5
             * category = 1,2,3,4,5
             * categoryIds = [1,2,3,4,5]
             */
            List<Long> categoryIds = Arrays.stream(category.split(","))
                    .map(Long::valueOf).collect(toList());

            return ProductSearchOption.builder()
                    .title(title)
                    .categories(categoryIds)
                    .build();
        }
    }
}
