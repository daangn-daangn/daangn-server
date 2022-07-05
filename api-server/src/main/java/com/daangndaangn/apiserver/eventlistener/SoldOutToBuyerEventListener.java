package com.daangndaangn.apiserver.eventlistener;

import com.daangndaangn.common.event.SoldOutToBuyerEvent;
import com.daangndaangn.common.message.SoldOutToBuyerMessage;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;


@Slf4j
public class SoldOutToBuyerEventListener implements AutoCloseable {

    @Value("${spring.kafka.topic.sold-out-to-buyer}")
    private String soldOutToBuyerTopic;

    private final EventBus eventBus;

    private final KafkaTemplate<String, SoldOutToBuyerMessage> kafkaTemplate;

    public SoldOutToBuyerEventListener(EventBus eventBus,
                                       KafkaTemplate<String, SoldOutToBuyerMessage> kafkaTemplate) {

        this.eventBus = eventBus;
        this.kafkaTemplate = kafkaTemplate;

        eventBus.register(this);
    }

    @Subscribe
    public void handleSoldOutToBuyerEvent(SoldOutToBuyerEvent event) {
        Long productId = event.getProductId();
        String productName = event.getProductName();
        Long sellerId = event.getSellerId();
        Long buyerId = event.getBuyerId();

        try {
            kafkaTemplate.send(soldOutToBuyerTopic, SoldOutToBuyerMessage.from(event));
        } catch (Exception e) {
            log.error("Got error while handling event SoldOutToBuyerEvent " + event, e);
        }
    }

    @Override
    public void close() throws Exception {
        eventBus.unregister(this);
    }
}
