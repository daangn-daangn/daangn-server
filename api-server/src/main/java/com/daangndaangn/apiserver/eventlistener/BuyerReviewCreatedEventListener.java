package com.daangndaangn.apiserver.eventlistener;

import com.daangndaangn.common.event.BuyerReviewCreatedEvent;
import com.daangndaangn.common.message.BuyerReviewCreatedMessage;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Slf4j
public class BuyerReviewCreatedEventListener implements AutoCloseable {

    @Value("${spring.kafka.topic.buyer-review-created}")
    private String buyerReviewCreatedTopic;

    private final EventBus eventBus;

    private final KafkaTemplate<String, BuyerReviewCreatedMessage> kafkaTemplate;

    public BuyerReviewCreatedEventListener(EventBus eventBus,
                                       KafkaTemplate<String, BuyerReviewCreatedMessage> kafkaTemplate) {

        this.eventBus = eventBus;
        this.kafkaTemplate = kafkaTemplate;

        eventBus.register(this);
    }

    @Subscribe
    public void handleBuyerReviewCreatedEvent(BuyerReviewCreatedEvent event) {
        Long sellerId = event.getSellerId();
        Long reviewerId = event.getReviewerId();

        ListenableFuture<SendResult<String, BuyerReviewCreatedMessage>> future =
            kafkaTemplate.send(buyerReviewCreatedTopic, BuyerReviewCreatedMessage.from(event));

        future.addCallback(new ListenableFutureCallback<>() {
            @Override
            public void onFailure(Throwable ex) {
                log.warn("handleBuyerReviewCreatedEvent exception occurred with sellerId: {}, reviewerId: {}: {}",
                        sellerId, reviewerId, ex.getMessage(), ex);
            }

            @Override
            public void onSuccess(SendResult<String, BuyerReviewCreatedMessage> result) {
            }
        });
    }

    @Override
    public void close() throws Exception {
        eventBus.unregister(this);
    }
}
