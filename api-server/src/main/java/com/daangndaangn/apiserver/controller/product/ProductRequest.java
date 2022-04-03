package com.daangndaangn.apiserver.controller.product;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import java.util.List;

public class ProductRequest {

    @Getter
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class CreateRequest {
        @NotNull
        private String title;
        @NotNull
        private String name;
        @NotNull
        private Long category;
        @NotNull
        private Long price;
        private String description;
        private List<String> imgUrlList;
    }

    @Getter
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class UpdateRequest {
        @NotNull
        private String title;
        @NotNull
        private String name;
        @NotNull
        private Long category;
        @NotNull
        private Long price;
        private String description;
        private List<String> imgUrlList;
    }

}
