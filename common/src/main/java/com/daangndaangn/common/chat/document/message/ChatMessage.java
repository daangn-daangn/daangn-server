package com.daangndaangn.common.chat.document.message;

import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;


/**
 * 채팅 메시지
 *
 * message는 이미지 요청 응답인 경우 null. 고로 getter returns Optional
 * imgUrls는 메시지, 좌표 응답인 경우 emptyList. 사용의 편의성을 위해 new ArrayList<>()로 초기화
 */
@ToString
@Getter
@NoArgsConstructor
public class ChatMessage {

    private Long senderId;
    private MessageType messageType;
    private String message;
    private List<String> imgUrls = new ArrayList<>();
    private LocalDateTime createdAt;

    public Optional<String> getMessage() {
        return ofNullable(message);
    }

    public static ChatMessage ofMessage(Long senderId,
                                        MessageType messageType,
                                        String message) {

        checkArgument(senderId != null, "senderId 값은 필수입니다.");
        checkArgument(messageType != null, "messageType 값은 필수입니다.");
        checkArgument(isNotEmpty(message), "message 값은 필수입니다.");

        return ChatMessage.builder()
                .senderId(senderId)
                .messageType(messageType)
                .message(message)
                .imgUrls(null)
                .build();
    }

    public static ChatMessage ofImage(Long senderId, List<String> imgUrls) {
        checkArgument(senderId != null, "senderId 값은 필수입니다.");
        checkArgument(imgUrls != null, "imgUrls 값은 필수입니다.");
        checkArgument(imgUrls.size() <= 10, "message 값은 필수입니다.");

        return ChatMessage.builder()
                .senderId(senderId)
                .messageType(MessageType.IMAGE)
                .message(null)
                .imgUrls(imgUrls)
                .build();
    }

    @Builder(access = AccessLevel.PRIVATE)
    private ChatMessage(Long senderId, MessageType messageType, String message, List<String> imgUrls) {
        this.senderId = senderId;
        this.messageType = messageType;
        this.message = message;
        this.imgUrls = imgUrls;
        this.createdAt = LocalDateTime.now();
    }
}
