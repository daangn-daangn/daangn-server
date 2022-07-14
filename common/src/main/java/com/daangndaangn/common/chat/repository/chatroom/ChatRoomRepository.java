package com.daangndaangn.common.chat.repository.chatroom;

import com.daangndaangn.common.chat.document.ChatRoom;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRoomRepository extends MongoRepository<ChatRoom, String>, ChatRoomCustomRepository {

    /**
     * ChatRoom 조회 시 chat_messages 최상위 1건만 가져오기 위한 전처리
     */
    @Query(value = "{ $and: [ {'productId': :#{#productId}}, {'identifier': :#{#identifier}} ] }",
            fields = "{'chat_messages': { $slice: -1 }}"
    )
    Optional<ChatRoom> findByProductIdAndIdentifier(@Param("productId") Long productId,
                                                    @Param("identifier") String identifier);

    /**
     * ChatRoom 조회 시 chat_messages 최상위 1건만 가져오기 위한 전처리
     */
    @Query(value = "{'productId': :#{#productId} }",
            fields = "{'chat_messages': { $slice: -1 }}"
    )
    List<ChatRoom> findAllByProductId(@Param("productId") Long productId, Pageable pageable);

    /**
     * ChatRoom 조회 시 chat_messages 최상위 1건만 가져오기 위한 전처리
     */
    @Query(value = "{'id': :#{#id} }",
            fields = "{'chat_messages': { $slice: -1 }}"
    )
    Optional<ChatRoom> findChatRoomById(@Param("id") String id);

    /**
     * ChatRoom 조회 시 chat_messages 최상위 1건만 가져오기 위한 전처리
     */
    @Query(value = "{'id': { $in : :#{#chatRoomIds} } }",
            fields = "{'chat_messages': { $slice: -1 }}"
    )
    List<ChatRoom> findAllByChatRoomIds(@Param("chatRoomIds") List<String> chatRoomIds, Sort sort);


    Long countAllByProductId(Long productId);

    boolean existsByProductIdAndIdentifier(Long productId, String identifier);

    /**
     * 채팅방 메시지 목록 조회
     *
     * parameter binding:  :#{#boundParamName} OR ?#{#boundParamName}
     */
    @Query(value = "{'id': :#{#id} }",
           fields = "{'chat_messages': { $slice: [:#{#page}, :#{#size}] }}"
    )
    Optional<ChatRoom> findChatRoomWithChatMessages(@Param("id") String id,
                                                    @Param("page") int page,
                                                    @Param("size") int size);

    /**
     * 현재 채팅방 내 총 채팅 갯수 구하기
     */
    @Aggregation(
        pipeline = {"{'$match' :{'id': :#{#id}} }",
                    "{'$project': { item: 1, numberOfChatMessages: { $size: '$chat_messages'}}}"
        }
    )
    Long getChatRoomMessageSize(@Param("id") String id);
}
