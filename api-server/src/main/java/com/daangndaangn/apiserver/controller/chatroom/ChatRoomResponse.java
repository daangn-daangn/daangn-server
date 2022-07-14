package com.daangndaangn.apiserver.controller.chatroom;

import com.daangndaangn.common.api.entity.product.Product;
import com.daangndaangn.common.api.entity.user.User;
import com.daangndaangn.common.chat.document.ChatRoom;
import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

import static org.apache.commons.lang3.ObjectUtils.isEmpty;

public class ChatRoomResponse {

    @Getter
    @AllArgsConstructor
    @JsonNaming(SnakeCaseStrategy.class)
    public static class CreateResponse {
        private String roomId;

        public static CreateResponse from(String chattingRoomId) {
            return new CreateResponse(chattingRoomId);
        }
    }

    @Getter
    @Builder
    @JsonNaming(SnakeCaseStrategy.class)
    public static class SimpleResponse {
        private String chatRoomId;
        private String participant;
        private String participantImage;
        private String location;
        private String productImage;
        private String lastChat;
        private Long notReadChatCount;
        private Long pageOffset;
        private Integer pageSize;
        private LocalDateTime updatedAt;

        public static SimpleResponse of(ChatRoom chatRoom,
                                        User user,
                                        String profileImage,
                                        long pageOffset,
                                        int pageSize,
                                        String productImage,
                                        long notReadChatCount) {

            return SimpleResponse.builder()
                    .chatRoomId(chatRoom.getId())
                    .participant(user.getNickname())
                    .participantImage(profileImage)
                    .location(isEmpty(user.getLocation()) ?
                            null : user.getLocation().getAddress())
                    .productImage(productImage)
                    .lastChat(isEmpty(chatRoom.getChatMessages()) ?
                            null : chatRoom.getChatMessages().get(0).getMessage().orElseGet(() -> "사진을 보냈습니다."))
                    .notReadChatCount(notReadChatCount)
                    .pageOffset(pageOffset)
                    .pageSize(pageSize)
                    .updatedAt(chatRoom.getUpdatedAt())
                    .build();
        }
    }

    @Getter
    @Builder
    @JsonNaming(SnakeCaseStrategy.class)
    public static class DetailResponse {
        private String chatRoomId;

        private Long participantId;
        private String participantNickname;
        private Double participantManner;
        private String participantImage;
        private Boolean out;

        private Long productId;
        private String productImage;
        private Long productPrice;
        private String productTitle;
        private String productState;

        public static DetailResponse of(ChatRoom chatRoom,
                                        boolean out,
                                        User user,
                                        String profileImage,
                                        Product product,
                                        String productImage) {

            return DetailResponse.builder()
                    .chatRoomId(chatRoom.getId())
                    .participantId(user.getId())
                    .participantNickname(user.getNickname())
                    .participantManner(user.getManner())
                    .participantImage(profileImage)
                    .out(out)
                    .productId(product.getId())
                    .productImage(productImage)
                    .productPrice(product.getPrice())
                    .productTitle(product.getTitle())
                    .productState(product.getProductState().getState())
                    .build();
        }
    }

    @Getter
    @AllArgsConstructor
    @JsonNaming(SnakeCaseStrategy.class)
    public static class BuyerResponse {
        private Long id;
        private String name;

        public static BuyerResponse from(User user) {
            return new BuyerResponse(user.getId(), user.getNickname());
        }
    }

}
