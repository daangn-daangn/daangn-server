package com.daangndaangn.chatserver.service;

import com.daangndaangn.common.api.entity.product.Product;
import com.daangndaangn.common.api.repository.product.ProductRepository;
import com.daangndaangn.common.chat.document.ChattingRoom;
import com.daangndaangn.common.chat.repository.ChattingRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChattingRoomService {

    private final ChattingRoomRepository chattingRoomRepository;
    private final ParticipantService participantService;
    private final ProductRepository productRepository;

    @Transactional(value = "mongoTransactionManager")
    public ChattingRoom create(Long productId, List<Long> userIds) {
        checkArgument(productId != null, "productId는 null일 수 없습니다.");
        checkArgument(userIds != null, "userIds는 null일 수 없습니다.");
        checkArgument(userIds.size() == 2, "userIds size는 2여야 합니다.");
        checkArgument(userIds.get(0) != null && userIds.get(1) != null, "userIds 내 값은 null일 수 없습니다.");

        Long userId1 = userIds.get(0);
        Long userId2 = userIds.get(1);

        String identifier = toIdentifier(userId1, userId2);

        //TODO 예외 변경
        if (chattingRoomRepository.existsByProductIdAndIdentifier(productId, identifier)) {
            return chattingRoomRepository.findByProductIdAndIdentifier(productId, identifier)
                    .orElseThrow(() -> new RuntimeException("chattingRoom을 찾을 수 없습니다."));
        }

        String productImage = productRepository.findById(productId).map(Product::getThumbNailImage).orElse(null);

        long firstUserId = Math.min(userId1, userId2);
        long secondUserId = Math.max(userId1, userId2);

        ChattingRoom chattingRoom = ChattingRoom.builder()
                                                .productId(productId)
                                                .productImage(productImage)
                                                .firstUserId(firstUserId)
                                                .secondUserId(secondUserId)
                                                .identifier(identifier)
                                                .build();

        ChattingRoom savedChattingRoom = chattingRoomRepository.save(chattingRoom);

        if (productId.equals(5L)) {
            throw new RuntimeException("productId.equals(5L)");
        }

        participantService.create(userId1, savedChattingRoom);
        participantService.create(userId2, savedChattingRoom);

        return savedChattingRoom;
    }

    public List<ChattingRoom> getChattingRooms(Long userId, Pageable pageable) {
        checkArgument(userId != null, "userId는 null일 수 없습니다.");
        return chattingRoomRepository.findAllByFirstUserIdOrSecondUserId(userId, userId, pageable);
    }

    private String toIdentifier(Long userId1, Long userId2) {
        checkArgument(userId1 != null, "userId1은 null일 수 없습니다.");
        checkArgument(userId2 != null, "userId2는 null일 수 없습니다.");

        long firstUserId = Math.min(userId1, userId2);
        long secondUserId = Math.max(userId1, userId2);

        return firstUserId + "-" + secondUserId;
    }
}
