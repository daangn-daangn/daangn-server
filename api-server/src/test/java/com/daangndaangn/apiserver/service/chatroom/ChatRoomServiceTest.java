package com.daangndaangn.apiserver.service.chatroom;

import com.daangndaangn.apiserver.service.participant.ParticipantService;
import com.daangndaangn.apiserver.service.product.ProductService;
import com.daangndaangn.apiserver.service.user.UserService;
import com.daangndaangn.common.api.entity.category.Category;
import com.daangndaangn.common.api.entity.product.Product;
import com.daangndaangn.common.api.entity.user.Location;
import com.daangndaangn.common.api.entity.user.User;
import com.daangndaangn.common.chat.document.ChatRoom;
import com.daangndaangn.common.chat.document.Participant;
import com.daangndaangn.common.chat.repository.chatroom.ChatRoomRepository;
import com.daangndaangn.common.error.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChatRoomServiceTest {

    @InjectMocks
    private ChatRoomServiceImpl chatRoomService;

    @Mock
    private ProductService productService;

    @Mock
    private ParticipantService participantService;

    @Mock
    private UserService userService;

    @Mock
    private ChatRoomRepository chatRoomRepository;

    private User mockUser1, mockUser2;
    private Product mockProduct;
    private ChatRoom mockChatRoom;
    private Participant mockParticipant;

    @BeforeEach
    public void init() {
        Category mockCategory = Category.builder().id(1L).name("mockCategory").build();
        mockUser1 = User.builder()
                .id(1L)
                .oauthId(12345L)
                .build();

        mockUser1.update("testNickname", Location.from("test address"));

        mockUser2 = User.builder()
                .id(2L)
                .oauthId(56789L)
                .build();

        mockUser2.update("testNickname", Location.from("test address"));

        mockProduct = Product.builder()
                .id(1L)
                .seller(mockUser1)
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
                .userId(mockUser1.getId())
                .chatRoomId(mockChatRoom.getId())
                .build();
    }

    @Test
    @DisplayName("물품ID와_사용자_ID_list를_받으면_채팅방을_생성할_수_있다")
    public void create1() throws ExecutionException, InterruptedException {
        //given
        given(userService.existById(anyLong())).willReturn(true);

        given(chatRoomRepository.existsByProductIdAndIdentifier(anyLong(), anyString()))
                .willReturn(false);

        given(productService.getProduct(anyLong())).willReturn(mockProduct);

        given(chatRoomRepository.save(any())).willReturn(mockChatRoom);

        given(participantService.create(any(), any())).willReturn(mockParticipant);

        //when
        ChatRoom chatRoom = chatRoomService.create(mockProduct.getId(), List.of(1L, 2L)).get();

        //then
        verify(userService, times(2)).existById(anyLong());
        verify(chatRoomRepository).existsByProductIdAndIdentifier(anyLong(), anyString());
        verify(chatRoomRepository, never()).findByProductIdAndIdentifier(anyLong(), anyString());
        verify(productService).getProduct(anyLong());
        verify(chatRoomRepository).save(any());
        verify(participantService, times(2)).create(any(), any());

        assertThat(chatRoom.getId()).isEqualTo(mockChatRoom.getId());
        assertThat(chatRoom.getProductId()).isEqualTo(mockProduct.getId());
        assertThat(chatRoom.getProductImage()).isNull();
    }

    @Test
    @DisplayName("이미_채팅방이_존재하는_경우_기존의_채팅방을_리턴한다")
    public void create2() throws ExecutionException, InterruptedException {
        //given
        given(userService.existById(anyLong())).willReturn(true);

        given(chatRoomRepository.existsByProductIdAndIdentifier(anyLong(), anyString()))
                .willReturn(true);

        given(chatRoomRepository.findByProductIdAndIdentifier(anyLong(), anyString()))
                .willReturn(Optional.ofNullable(mockChatRoom));

        //when
        ChatRoom chatRoom = chatRoomService.create(mockProduct.getId(), List.of(1L, 2L)).get();

        //then
        verify(userService, times(2)).existById(anyLong());
        verify(chatRoomRepository).existsByProductIdAndIdentifier(anyLong(), anyString());
        verify(chatRoomRepository).findByProductIdAndIdentifier(anyLong(), anyString());
        verify(productService, never()).getProduct(anyLong());
        verify(chatRoomRepository, never()).save(any());
        verify(participantService, never()).create(any(), any());

        assertThat(chatRoom.getId()).isEqualTo(mockChatRoom.getId());
        assertThat(chatRoom.getProductId()).isEqualTo(mockProduct.getId());
        assertThat(chatRoom.getProductImage()).isNull();
    }

    @Test
    @DisplayName("채팅방_생성_시_존재하지_않는_UserId인_경우_예외를_반환한다")
    public void create3() {
        //given
        given(userService.existById(anyLong())).willReturn(false);

        //when
        assertThrows(NotFoundException.class, () -> chatRoomService.create(mockProduct.getId(), List.of(1L, 2L)));

        //then
        verify(userService).existById(anyLong());
        verify(chatRoomRepository, never()).existsByProductIdAndIdentifier(anyLong(), anyString());
        verify(chatRoomRepository, never()).findByProductIdAndIdentifier(anyLong(), anyString());
        verify(productService, never()).getProduct(anyLong());
        verify(chatRoomRepository, never()).save(any());
        verify(participantService, never()).create(any(), any());
    }

    @Test
    @DisplayName("이미_존재하는_채팅방_조회_시_에러가_발생하는_경우_예외를_반환한다")
    public void create4() {
        //given
        given(userService.existById(anyLong())).willReturn(true);

        given(chatRoomRepository.existsByProductIdAndIdentifier(anyLong(), anyString()))
                .willReturn(true);

        given(chatRoomRepository.findByProductIdAndIdentifier(anyLong(), anyString()))
                .willReturn(Optional.ofNullable(null));

        //when
        assertThrows(NotFoundException.class, () -> chatRoomService.create(mockProduct.getId(), List.of(1L, 2L)));

        //then
        verify(userService, times(2)).existById(anyLong());
        verify(chatRoomRepository).existsByProductIdAndIdentifier(anyLong(), anyString());
        verify(chatRoomRepository).findByProductIdAndIdentifier(anyLong(), anyString());
        verify(productService, never()).getProduct(anyLong());
        verify(chatRoomRepository, never()).save(any());
        verify(participantService, never()).create(any(), any());
    }

    @Test
    @DisplayName("product_id가_없으면_채팅방을_생성하지_않고_예외를_반환한다")
    public void create5() {
        //given
        given(userService.existById(anyLong())).willReturn(true);

        given(chatRoomRepository.existsByProductIdAndIdentifier(anyLong(), anyString()))
                .willReturn(false);

        given(productService.getProduct(anyLong())).willThrow(NotFoundException.class);;

        //when
        assertThrows(NotFoundException.class, () -> chatRoomService.create(mockProduct.getId(), List.of(1L, 2L)));

        //then
        verify(userService, times(2)).existById(anyLong());
        verify(chatRoomRepository).existsByProductIdAndIdentifier(anyLong(), anyString());
        verify(chatRoomRepository, never()).findByProductIdAndIdentifier(anyLong(), anyString());
        verify(productService).getProduct(anyLong());
        verify(chatRoomRepository, never()).save(any());
        verify(participantService, never()).create(any(), any());
    }

    @Test
    @DisplayName("participant를_생성하다가_에러가_발생하는_경우_예외를_반환한다")
    public void create6() {
        //given
        given(userService.existById(anyLong())).willReturn(true);

        given(chatRoomRepository.existsByProductIdAndIdentifier(anyLong(), anyString()))
                .willReturn(false);

        given(productService.getProduct(anyLong())).willReturn(mockProduct);

        given(chatRoomRepository.save(any())).willReturn(mockChatRoom);

        given(participantService.create(any(), any())).willThrow(IllegalArgumentException.class);

        //when
        assertThrows(IllegalArgumentException.class, () -> chatRoomService.create(mockProduct.getId(), List.of(1L, 2L)));

        //then
        verify(userService, times(2)).existById(anyLong());
        verify(chatRoomRepository).existsByProductIdAndIdentifier(anyLong(), anyString());
        verify(chatRoomRepository, never()).findByProductIdAndIdentifier(anyLong(), anyString());
        verify(productService).getProduct(anyLong());
        verify(chatRoomRepository).save(any());
        verify(participantService).create(any(), any());
    }

    @Test
    @DisplayName("chatRoom을_생성하지_못하는_경우_예외를_반환한다")
    public void create7() {
        //given
        given(userService.existById(anyLong())).willReturn(true);

        given(chatRoomRepository.existsByProductIdAndIdentifier(anyLong(), anyString()))
                .willReturn(false);

        given(productService.getProduct(anyLong())).willReturn(mockProduct);

        given(chatRoomRepository.save(any())).willThrow(IllegalArgumentException.class);

        //when
        assertThrows(IllegalArgumentException.class, () -> chatRoomService.create(mockProduct.getId(), List.of(1L, 2L)));

        //then
        verify(userService, times(2)).existById(anyLong());
        verify(chatRoomRepository).existsByProductIdAndIdentifier(anyLong(), anyString());
        verify(chatRoomRepository, never()).findByProductIdAndIdentifier(anyLong(), anyString());
        verify(productService).getProduct(anyLong());
        verify(chatRoomRepository).save(any());
        verify(participantService, never()).create(any(), any());
    }

    @Test
    @DisplayName("채팅방을_생성하려는_두_UserId가_같은경우_예외를_반환한다")
    public void create8() {
        //when
        assertThrows(IllegalArgumentException.class, () -> chatRoomService.create(mockProduct.getId(), List.of(1L, 1L)));
    }

    @Test
    @DisplayName("userId_순서가_달라도_같은_Identifier를_반환한다")
    public void toIdentifier() {
        //given
        //when
        String identifier1 = chatRoomService.toIdentifier(1L, 2L);
        String identifier2 = chatRoomService.toIdentifier(2L, 1L);

        //then
        assertThat(identifier1).isNotNull();
        assertThat(identifier2).isNotNull();
        assertThat(identifier1).isEqualTo(identifier2);
    }

    @Test
    @DisplayName("userId로_사용자가_들어가있는_채팅방을_조회할_수_있다")
    public void getChatRooms1() {
        //given
        Pageable pageable = PageRequest.of(0, 5);
        given(chatRoomRepository.findAllByChatRoomIds(anyList(), any()))
                .willReturn(List.of(mockChatRoom, mockChatRoom));

        //when
        List<ChatRoom> chattingRooms = chatRoomService.getChatRooms(mockUser1.getId(), pageable);

        //then
        verify(chatRoomRepository).findAllByChatRoomIds(anyList(), any());
        assertThat(chattingRooms.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("userId로_사용자가_들어가있는_채팅방이_없을_시_빈_리스트를_반환한다")
    public void getChatRooms2() {
        //given
        Pageable pageable = PageRequest.of(0, 5);
        given(chatRoomRepository.findAllByChatRoomIds(anyList(), any()))
                .willReturn(Collections.emptyList());

        //when
        List<ChatRoom> chattingRooms = chatRoomService.getChatRooms(mockUser1.getId(), pageable);

        //then
        verify(chatRoomRepository).findAllByChatRoomIds(anyList(), any());
        assertThat(chattingRooms.size()).isEqualTo(0);
    }

    @Test
    @DisplayName("id에_맞는_채팅방을_조회할_수_있다")
    public void getChatRoom1() {
        //given
        String testChatRoomId = "1qa2ws3ed";

        ChatRoom chatRoom = ChatRoom.builder()
                .id(testChatRoomId)
                .productId(mockProduct.getId())
                .productImage(null)
                .firstUserId(1L)
                .secondUserId(2L)
                .identifier("1-2")
                .build();

        given(chatRoomRepository.findChatRoomById(anyString())).willReturn(Optional.ofNullable(chatRoom));

        //when
        ChatRoom findChatRoom = chatRoomService.getChatRoom(testChatRoomId);

        //then
        verify(chatRoomRepository).findChatRoomById(anyString());
        assertThat(findChatRoom.getId()).isEqualTo(chatRoom.getId());
        assertThat(findChatRoom.getProductId()).isEqualTo(chatRoom.getProductId());
        assertThat(findChatRoom.getFirstUserId()).isEqualTo(chatRoom.getFirstUserId());
        assertThat(findChatRoom.getSecondUserId()).isEqualTo(chatRoom.getSecondUserId());
        assertThat(findChatRoom.getIdentifier()).isEqualTo(chatRoom.getIdentifier());
    }

    @Test
    @DisplayName("id에_맞는_채팅방이_없을_시_예외를_반환한다")
    public void getChatRoom2() {
        //given
        String testChatRoomId = "1qa2ws3ed";
        given(chatRoomRepository.findChatRoomById(anyString())).willReturn(Optional.empty());

        //when
        assertThrows(NotFoundException.class, () -> chatRoomService.getChatRoom(testChatRoomId));

        //then
        verify(chatRoomRepository).findChatRoomById(anyString());
    }

    @Test
    @DisplayName("id_입력_시_채팅룸에_존재하는_메시지_갯수를_반환한다")
    public void getChatRoomMessageSize() {
        //given
        long chatRoomMessageSize = 6L;
        String mockChatRoomId = "mockChatRoomId";
        given(chatRoomRepository.getChatRoomMessageSize(anyString())).willReturn(chatRoomMessageSize);

        //when
        long result = chatRoomService.getChatRoomMessageSize(mockChatRoomId);

        //then
        assertThat(result).isEqualTo(chatRoomMessageSize);
        verify(chatRoomRepository).getChatRoomMessageSize(anyString());
    }
}