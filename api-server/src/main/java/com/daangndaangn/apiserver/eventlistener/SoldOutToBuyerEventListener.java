package com.daangndaangn.apiserver.eventlistener;

import com.daangndaangn.common.event.SoldOutToBuyerEvent;
import com.daangndaangn.common.message.SoldOutToBuyerMessage;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;


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
        Long sellerId = event.getSellerId();
        Long buyerId = event.getBuyerId();

        ListenableFuture<SendResult<String, SoldOutToBuyerMessage>> future =
            kafkaTemplate.send(soldOutToBuyerTopic, SoldOutToBuyerMessage.from(event));

        future.addCallback(new ListenableFutureCallback<>() {
            @Override
            public void onFailure(Throwable ex) {
                log.warn("handleSoldOutToBuyerEvent exception occurred with productId: {}, sellerId: {}, buyerId: {}: {}",
                        productId, sellerId, buyerId, ex.getMessage(), ex);
            }

            @Override
            public void onSuccess(SendResult<String, SoldOutToBuyerMessage> result) {

            }
        });
    }

    @Override
    public void close() throws Exception {
        eventBus.unregister(this);
    }
}
