package com.daangndaangn.apiserver.service.chatting;

import com.daangndaangn.apiserver.error.NotFoundException;
import com.daangndaangn.apiserver.service.product.ProductService;
import com.daangndaangn.common.api.entity.product.Product;
import com.daangndaangn.common.chat.document.message.ChattingMessage;
import com.daangndaangn.common.chat.document.room.ChattingRoom;
import com.daangndaangn.common.chat.repository.message.ChattingMessageRepository;
import com.daangndaangn.common.chat.repository.room.ChattingRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class ChattingServiceImpl implements ChattingService{

    private final ChattingRoomRepository chattingRoomRepository;
    private final ChattingMessageRepository chattingMessageRepository;
    private final ProductService productService;

    @Override
    public ChattingRoom findChatting(Long userId){
        return chattingRoomRepository.findByUserId(userId).orElseThrow(() -> new NotFoundException(ChattingRoom.class, String.format("userId = %s", userId)));
    }

    @Override
    public void createChatting(Long productId, Long userId) {
        Product product = productService.findProduct(productId);
        Long sellerId = product.getSeller().getId();

        ChattingMessage chattingMessage = chattingMessageRepository.save(new ChattingMessage());
        String roomId = chattingMessage.getId();

        ChattingRoom chattingRoomForUser = this.findChatting(userId);
        ChattingRoom chattingRoomForSeller = this.findChatting(sellerId);

        chattingRoomForUser.addChatting(productId, roomId);
        chattingRoomForSeller.addChatting(productId, roomId);

        //변경 감지에 대해서는 찾아봐야 함.
        chattingRoomRepository.save(chattingRoomForUser);
        chattingRoomRepository.save(chattingRoomForSeller);
    }
}
