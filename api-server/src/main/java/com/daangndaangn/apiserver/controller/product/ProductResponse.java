package com.daangndaangn.apiserver.controller.product;

import com.daangndaangn.common.api.entity.product.Product;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class ProductResponse {

    @Getter
    @Builder
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class CreateResponse {
        private Long id;

        public static ProductResponse.CreateResponse from(Long id){
            return new CreateResponse(id);
        }
        private CreateResponse(Long id){
            this.id = id;
        }
    }

    @Getter
    @Builder
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class GetResponse {
        private String title;
        private String location;
        private String name;
        private String category;
        private String state;
        private String description;
        private Long chattingCount;
        private Long price;
        private LocalDateTime createdAt;
        private List<String> imgUrlList;

        private String userNickname;
        private Double manner;

        public static ProductResponse.GetResponse from(Product product){
            return GetResponse.builder()
                    .title(product.getTitle())
                    .location(product.getLocation().getAddress())
                    .name(product.getName())
                    .category(product.getCategory().getName())
                    .state(product.getState().getState())
                    .description(product.getDescription())
                    .chattingCount(product.getChattingCount())
                    .price(product.getPrice())
                    .createdAt(product.getCreatedAt())
                    .userNickname(product.getSeller().getNickname())
                    .manner(product.getSeller().getManner())
                    .imgUrlList(product.getProductImageList().stream().map(url -> url.getImageUrl()).collect(Collectors.toList()))
                    .build();
        }
    }

    @Getter
    @Builder
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class GetListResponse {
        private String title;
        private String name;
        private String location;
        private String thumbNailImage;
        private Long price;
        private Long chattingCount;
        private LocalDateTime createdAt;


        public static ProductResponse.GetListResponse from(Product product){
            return GetListResponse.builder()
                    .title(product.getTitle())
                    .name(product.getName())
                    .location(product.getLocation().getAddress())
                    .thumbNailImage(product.getThumbNailImage())
                    .price(product.getPrice())
                    .chattingCount(product.getChattingCount())
                    .createdAt(product.getCreatedAt())
                    .build();
        }
    }

}
