package com.daangndaangn.apiserver.controller.product;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;

import javax.validation.constraints.*;
import java.util.ArrayList;
import java.util.List;

public class ProductRequest {

    @Getter
    @JsonNaming(SnakeCaseStrategy.class)
    public static class CreateRequest {
        @NotNull(message = "categoryId 값은 필수입니다.")
        private Long categoryId;
        @NotBlank(message = "title 값은 필수입니다.")
        private String title;
        @NotNull(message = "price 값은 필수입니다.")
        private Long price;
        @NotBlank(message = "description 값은 필수입니다.")
        private String description;
        @Size(max = 10)
        private List<String> productImages = new ArrayList<>();
    }

    @Getter
    @JsonNaming(SnakeCaseStrategy.class)
    public static class UpdateRequest {
        @NotNull(message = "categoryId 값은 필수입니다.")
        private Long categoryId;
        @NotBlank(message = "title 값은 필수입니다.")
        private String title;
        @NotNull(message = "price 값은 필수입니다.")
        private Long price;
        @NotBlank(message = "description 값은 필수입니다.")
        private String description;
    }

    @Getter
    @JsonNaming(SnakeCaseStrategy.class)
    public static class UpdateStateRequest {
        @Min(value = 0)
        @Max(value = 3)
        @NotNull(message = "state 값은 필수입니다.")
        private Integer state;
        private Long buyerId;
    }

}
