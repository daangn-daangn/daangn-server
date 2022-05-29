package com.daangndaangn.common.chat.repository;

import com.daangndaangn.common.chat.document.ChattingInfo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChattingInfoRepository extends MongoRepository<ChattingInfo, String> {

    Long countAllByProductId(Long productId);

    List<ChattingInfo> findAllBySellerIdOrBuyerId(Long sellerId, Long buyerId);
}
