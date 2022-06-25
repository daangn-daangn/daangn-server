package com.daangndaangn.apiserver.eventlistener;

import com.daangndaangn.apiserver.service.favorite.FavoriteProductService;
import com.daangndaangn.common.event.SoldOutEvent;
import com.daangndaangn.common.message.SoldOutMessage;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Slf4j
public class SoldOutEventListener implements AutoCloseable {

    @Value("${spring.kafka.topic.sold-out}")
    private String soldOutTopic;

    private final EventBus eventBus;

    private final KafkaTemplate<String, SoldOutMessage> kafkaTemplate;

    private final FavoriteProductService favoriteProductService;

    public SoldOutEventListener(EventBus eventBus,
                                KafkaTemplate<String, SoldOutMessage> kafkaTemplate,
                                FavoriteProductService favoriteProductService) {

        this.eventBus = eventBus;
        this.kafkaTemplate = kafkaTemplate;
        this.favoriteProductService = favoriteProductService;

        eventBus.register(this);
    }

    @Subscribe
    public void handleSoldOutEvent(SoldOutEvent event) {
        Long productId = event.getProductId();
        String productName = event.getProductName();
        List<Long> userIds = favoriteProductService.getFavoriteProductsByProduct(productId)
                .stream()
                .map(favoriteProduct -> favoriteProduct.getUser().getId())
                .collect(toList());

        log.info("product {}, productId {} soldOut! send to user: {}", productName, productId, userIds);

        try {
            log.info("Try to send push for {}", event);
            kafkaTemplate.send(soldOutTopic, SoldOutMessage.of(event, userIds));
        } catch (Exception e) {
            log.error("Got error while handling event SoldOutEvent " + event, e);
        }
    }

    @Override
    public void close() throws Exception {
        eventBus.unregister(this);
    }
}
