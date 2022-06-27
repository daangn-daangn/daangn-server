package com.daangndaangn.chatserver.service.message;

import com.daangndaangn.common.api.entity.category.Category;
import com.daangndaangn.common.api.entity.product.Product;
import com.daangndaangn.common.api.entity.user.Location;
import com.daangndaangn.common.api.entity.user.User;
import com.daangndaangn.common.chat.document.ChatRoom;
import com.daangndaangn.common.chat.document.Participant;
import com.daangndaangn.common.chat.document.message.MessageType;
import com.daangndaangn.common.chat.repository.chatroom.ChatRoomRepository;
import com.daangndaangn.common.chat.repository.participant.ParticipantRepository;
import com.daangndaangn.common.error.NotFoundException;
import com.daangndaangn.common.util.UploadUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChatMessageServiceTest {

    @InjectMocks
    private ChatMessageServiceImpl chatMessageService;

    @Mock
    private ChatRoomRepository chatRoomRepository;

    @Mock
    private ParticipantRepository participantRepository;

    @Mock
    private UploadUtils uploadUtils;

    private User mockUser;
    private Product mockProduct;
    private ChatRoom mockChatRoom;
    private Participant mockParticipant;

    @BeforeEach
    public void init() {
        Category mockCategory = Category.builder().id(1L).name("testCategory").build();
        mockUser = User.builder()
                .id(1L)
                .oauthId(12345L)
                .build();

        mockUser.update("testNickname", Location.from("test Address"));

        mockProduct = Product.builder()
                .id(1L)
                .seller(mockUser)
                .category(mockCategory)
                .title("테스트 title 입니다.")
                .name("name")
                .price(1L)
                .description("테스트 description 입니다.")
                .build();

        mockChatRoom = ChatRoom.builder()
                .id("testChatRoomId")
                .productId(mockProduct.getId())
                .productImage(null)
                .firstUserId(1L)
                .secondUserId(2L)
                .identifier("1-2")
                .build();

        mockParticipant = Participant.builder()
                .userId(mockUser.getId())
                .chatRoomId(mockChatRoom.getId())
                .build();
    }

    @Test
    public void 올바르지_않은_메시지_타입은_예외를_반환한다() {
        //given
        String id = "testId";
        long senderId = 1L;
        int invalidMessageType1 = 0;
        int invalidMessageType2 = 4;
        String message = "test";
        List<String> mockImgUrls = List.of("test.jpg", "test1.jpg");

        //when
        assertThrows(IllegalArgumentException.class,
                () -> chatMessageService.addChatMessage(id, senderId, 2L, invalidMessageType1, message, mockImgUrls));

        assertThrows(IllegalArgumentException.class,
                () -> chatMessageService.addChatMessage(id, senderId, 3L, invalidMessageType2, message, mockImgUrls));

        //then
        verify(chatRoomRepository, never()).insertChatMessage(anyString(), any());
        verify(participantRepository, never()).synchronizeUpdatedAt(anyString(), anyList(), any());
    }

    @Test
    public void 일반_메시지_저장_시_imgUrl_필드가_비어있어야_한다() {
        //given
        String id = "testId";
        long senderId = 1L;
        int imageMessageCode = MessageType.MESSAGE.getCode();
        String message = "NORMAL_MESSAGE";
        List<String> emptyList = Collections.emptyList();

        //when
        chatMessageService.addChatMessage(id, senderId, 2L, imageMessageCode, message, emptyList);
        chatMessageService.addChatMessage(id, senderId, 2L, imageMessageCode, message, null);

        //then
        verify(chatRoomRepository, times(2)).insertChatMessage(anyString(), any());
        verify(participantRepository, times(2)).synchronizeUpdatedAt(anyString(), anyList(), any());
        verify(uploadUtils, never()).isNotImageFile(anyString());
    }

    @Test
    public void 일반_메시지_저장_시_imgUrl_필드가_있는_경우_예외를_반환한다() {
        //given
        String id = "testId";
        long senderId = 1L;
        int normalMessageCode = MessageType.MESSAGE.getCode();
        String message = "test";
        List<String> mockImgUrls = List.of("test.jpg", "test1.jpg");

        //when
        assertThrows(IllegalArgumentException.class,
                () -> chatMessageService.addChatMessage(id, senderId, 2L, normalMessageCode, message, mockImgUrls));

        //then
        verify(chatRoomRepository, never()).insertChatMessage(anyString(), any());
        verify(participantRepository, never()).synchronizeUpdatedAt(anyString(), anyList(), any());
    }

    @Test
    public void 좌표_메시지_저장_시_imgUrl_필드가_비어있어야_한다() {
        //given
        String id = "testId";
        long senderId = 1L;
        int imageMessageCode = MessageType.POSITION.getCode();
        String message = "123,456";
        List<String> emptyList = Collections.emptyList();

        //when
        chatMessageService.addChatMessage(id, senderId, 2L, imageMessageCode, message, emptyList);
        chatMessageService.addChatMessage(id, senderId, 2L, imageMessageCode, message, null);

        //then
        verify(chatRoomRepository, times(2)).insertChatMessage(anyString(), any());
        verify(participantRepository, times(2)).synchronizeUpdatedAt(anyString(), anyList(), any());
        verify(uploadUtils, never()).isNotImageFile(anyString());
    }

    @Test
    public void 좌표_메시지_저장_시_imgUrl_필드가_있는_경우_예외를_반환한다() {
        //given
        String id = "testId";
        long senderId = 1L;
        int posMessageCode = MessageType.POSITION.getCode();
        String message = "test";
        List<String> mockImgUrls = List.of("test.jpg", "test1.jpg");

        //when
        assertThrows(IllegalArgumentException.class,
                () -> chatMessageService.addChatMessage(id, senderId, 2L, posMessageCode, message, mockImgUrls));

        //then
        verify(chatRoomRepository, never()).insertChatMessage(anyString(), any());
        verify(participantRepository, never()).synchronizeUpdatedAt(anyString(), anyList(), any());
    }

    @Test
    public void 이미지_메시지_저장_시_message_필드가_비어있어야_한다() {
        //given
        given(uploadUtils.isNotImageFile(anyString())).willReturn(false);

        String id = "testId";
        long senderId = 1L;
        int imageMessageCode = MessageType.IMAGE.getCode();
        String nullMessage = null;
        String emptyMessage = "";
        String blankMessage = " ";
        List<String> mockImgUrls = List.of("test.jpg", "test1.jpg");

        //when
        chatMessageService.addChatMessage(id, senderId, 2L, imageMessageCode, nullMessage, mockImgUrls);
        chatMessageService.addChatMessage(id, senderId, 2L, imageMessageCode, emptyMessage, mockImgUrls);
        chatMessageService.addChatMessage(id, senderId, 2L, imageMessageCode, blankMessage, mockImgUrls);

        //then
        verify(chatRoomRepository, times(3)).insertChatMessage(anyString(), any());
        verify(participantRepository, times(3)).synchronizeUpdatedAt(anyString(), anyList(), any());
        verify(uploadUtils, times(6)).isNotImageFile(anyString());
    }

    @Test
    public void 이미지_메시지_저장_시_message_필드가_있는_경우_예외를_반환한다() {
        //given
        String id = "testId";
        long senderId = 1L;
        int imageMessageCode = MessageType.IMAGE.getCode();
        String message = "test";
        List<String> mockImgUrls = List.of("test.jpg", "test1.jpg");

        //when
        assertThrows(IllegalArgumentException.class,
                () -> chatMessageService.addChatMessage(id, senderId, 2L, imageMessageCode, message, mockImgUrls));

        //then
        verify(chatRoomRepository, never()).insertChatMessage(anyString(), any());
        verify(participantRepository, never()).synchronizeUpdatedAt(anyString(), anyList(), any());
    }



    @Test
    public void readMessageSize를_내가_속한_채팅방의_채팅_갯수만큼_업데이트_할_수_있다() {
        //given
        long testMessageSize = 25L;
        given(chatRoomRepository.getChatRoomMessageSize(anyString())).willReturn(testMessageSize);
        given(participantRepository.findByChatRoomIdAndUserId(anyString(), anyLong()))
                .willReturn(Optional.ofNullable(mockParticipant));
        given(participantRepository.save(any())).willReturn(mockParticipant);
        long beforeUpdate = mockParticipant.getReadMessageSize();

        //when
        chatMessageService.updateReadMessageSize(mockChatRoom.getId(), mockUser.getId());
        long afterUpdate = mockParticipant.getReadMessageSize();

        //then
        verify(chatRoomRepository).getChatRoomMessageSize(anyString());
        verify(participantRepository).findByChatRoomIdAndUserId(anyString(), anyLong());
        verify(participantRepository).save(any());
        assertThat(beforeUpdate).isEqualTo(0);
        assertThat(afterUpdate).isEqualTo(testMessageSize);
    }

    @Test
    public void 유효하지_않은_chatRoomId나_userId_입력시_readMessageSize를_업데이트_하지않고_예외를_반환한다() {
        //given
        String invalidChatRoomId = "invalidChatRoomId";
        Long invalidUserId = -1L;

        given(participantRepository.findByChatRoomIdAndUserId(anyString(), anyLong()))
                .willReturn(Optional.empty());

        //when
        assertThrows(NotFoundException.class,
                () -> chatMessageService.updateReadMessageSize(invalidChatRoomId, invalidUserId));

        //then
        verify(participantRepository).findByChatRoomIdAndUserId(anyString(), anyLong());
        verify(chatRoomRepository, never()).getChatRoomMessageSize(anyString());
        verify(participantRepository, never()).save(any());
    }
}