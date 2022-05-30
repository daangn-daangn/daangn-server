package com.daangndaangn.apiserver.controller.category;

import com.daangndaangn.common.api.entity.category.Category;
import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Getter;

public class CategoryResponse {

    @Getter
    @AllArgsConstructor
    @JsonNaming(SnakeCaseStrategy.class)
    public static class GetResponse {
        private Long id;
        private String name;

        public static GetResponse from(Category category) {
            return new GetResponse(category.getId(), category.getName());
        }
    }
}
