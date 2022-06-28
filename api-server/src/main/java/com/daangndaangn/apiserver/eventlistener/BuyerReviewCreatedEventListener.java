package com.daangndaangn.apiserver.eventlistener;

import com.daangndaangn.common.event.BuyerReviewCreatedEvent;
import com.daangndaangn.common.message.BuyerReviewCreatedMessage;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;

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

        log.info("buyerReview with saleReviewId: {} created! send to seller:{}",
                reviewerId, sellerId);

        try {
            log.info("Try to send push for {}", event);
            kafkaTemplate.send(buyerReviewCreatedTopic, BuyerReviewCreatedMessage.from(event));
        } catch (Exception e) {
            log.error("Got error while handling event BuyerReviewCreatedEvent " + event, e);
        }
    }

    @Override
    public void close() throws Exception {
        eventBus.unregister(this);
    }
}
