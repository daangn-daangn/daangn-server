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
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
    @DisplayName("올바르지_않은_메시지_타입은_예외를_반환한다")
    public void addChatMessage1() {
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
    @DisplayName("일반_메시지_저장_시_imgUrl_필드가_비어있어야_한다")
    public void addChatMessage2() {
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
    @DisplayName("일반_메시지_저장_시_imgUrl_필드가_있는_경우_예외를_반환한다")
    public void addChatMessage3() {
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
    @DisplayName("좌표_메시지_저장_시_imgUrl_필드가_비어있어야_한다")
    public void addChatMessage4() {
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
    @DisplayName("좌표_메시지_저장_시_imgUrl_필드가_있는_경우_예외를_반환한다")
    public void addChatMessage5() {
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
    @DisplayName("이미지_메시지_저장_시_message_필드가_비어있어야_한다")
    public void addChatMessage6() {
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
    @DisplayName("이미지_메시지_저장_시_message_필드가_있는_경우_예외를_반환한다")
    public void addChatMessage7() {
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
    @DisplayName("페이징 사이즈에 맞게 한 개의 ChatRoom을 조회할 수 있다")
    public void getChatRoomWithMessages1() {
        //given
        given(chatRoomRepository.findChatRoomWithChatMessages(anyString(), anyInt(), anyInt())).willReturn(Optional.ofNullable(mockChatRoom));

        int mockPageSize = 10;

        //when
        ChatRoom chatRoomWithMessages = chatMessageService.getChatRoomWithMessages(mockChatRoom.getId(), mockPageSize);

        //then
        assertThat(chatRoomWithMessages).isEqualTo(mockChatRoom);
        verify(chatRoomRepository).findChatRoomWithChatMessages(anyString(), anyInt(), anyInt());
    }

    @Test
    @DisplayName("ChatRoom 조회 시 없으면 예외를 반환한다")
    public void getChatRoomWithMessages2() {
        //given
        given(chatRoomRepository.findChatRoomWithChatMessages(anyString(), anyInt(), anyInt())).willReturn(Optional.empty());

        int mockPageSize = 10;

        //when
        Assertions.assertThatThrownBy(() -> chatMessageService.getChatRoomWithMessages(mockChatRoom.getId(), mockPageSize))
                .isInstanceOf(NotFoundException.class);

        //then
        verify(chatRoomRepository).findChatRoomWithChatMessages(anyString(), anyInt(), anyInt());
    }

    @Test
    @DisplayName("ChatRoom 조회 시 없으면 예외를 반환한다")
    public void getChatRoomWithMessages3() {
        //given
        String invalidChatRoomId = null;
        int invalidPageSize = -1;

        int mockPageSize = 10;

        //when
        Assertions.assertThatThrownBy(() -> chatMessageService.getChatRoomWithMessages(invalidChatRoomId, mockPageSize))
                .isInstanceOf(IllegalArgumentException.class);

        Assertions.assertThatThrownBy(() -> chatMessageService.getChatRoomWithMessages(mockChatRoom.getId(), invalidPageSize))
                .isInstanceOf(IllegalArgumentException.class);

        //then
        verify(chatRoomRepository, never()).findChatRoomWithChatMessages(anyString(), anyInt(), anyInt());
    }

    @Test
    @DisplayName("readMessageSize를_내가_속한_채팅방의_채팅_갯수만큼_업데이트_할_수_있다")
    public void updateReadMessageSize1() {
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
    @DisplayName("유효하지_않은_chatRoomId나_userId_입력시_readMessageSize를_업데이트_하지않고_예외를_반환한다")
    public void updateReadMessageSize2() {
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