package com.daangndaangn.apiserver.controller.product;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

public class ProductRequest {

    @Getter
    @JsonNaming(SnakeCaseStrategy.class)
    public static class CreateRequest {
        @NotNull
        private Long categoryId;
        @NotEmpty
        private String title;
        @NotEmpty
        private String name;
        @NotNull
        private Long price;
        @NotEmpty
        private String description;

        private List<String> productImages = new ArrayList<>();
    }

    @Getter
    @JsonNaming(SnakeCaseStrategy.class)
    public static class UpdateRequest {
        @NotNull
        private Long categoryId;
        @NotEmpty
        private String title;
        @NotEmpty
        private String name;
        @NotNull
        private Long price;
        @NotEmpty
        private String description;
    }

    @Getter
    @JsonNaming(SnakeCaseStrategy.class)
    public static class SoldOutRequest {
        @NotNull
        private Long buyerId;
    }

}
