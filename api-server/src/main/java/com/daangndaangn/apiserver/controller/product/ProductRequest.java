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
        private Long category;
        @NotNull
        private Long price;
        private String content;
        private List<String> imgUrl;

    }
}
