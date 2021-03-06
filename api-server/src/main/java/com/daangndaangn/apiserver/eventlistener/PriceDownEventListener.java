package com.daangndaangn.apiserver.eventlistener;

import com.daangndaangn.apiserver.service.favorite.FavoriteProductService;
import com.daangndaangn.common.event.PriceDownEvent;
import com.daangndaangn.common.message.PriceDownMessage;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Slf4j
public class PriceDownEventListener implements AutoCloseable {

    @Value("${spring.kafka.topic.price-down}")
    private String priceDownTopic;

    private final EventBus eventBus;

    private final KafkaTemplate<String, PriceDownMessage> kafkaTemplate;

    private final FavoriteProductService favoriteProductService;

    public PriceDownEventListener(EventBus eventBus,
                                KafkaTemplate<String, PriceDownMessage> kafkaTemplate,
                                FavoriteProductService favoriteProductService) {

        this.eventBus = eventBus;
        this.kafkaTemplate = kafkaTemplate;
        this.favoriteProductService = favoriteProductService;

        eventBus.register(this);
    }

    @Subscribe
    public void handlePriceDownEvent(PriceDownEvent event) {
        Long productId = event.getProductId();
        List<Long> userIds = favoriteProductService.getFavoriteProductsByProduct(productId)
                .stream()
                .map(favoriteProduct -> favoriteProduct.getUser().getId())
                .collect(toList());

        ListenableFuture<SendResult<String, PriceDownMessage>> future =
            kafkaTemplate.send(priceDownTopic, PriceDownMessage.of(event, userIds));

        future.addCallback(new ListenableFutureCallback<>() {
            @Override
            public void onFailure(Throwable ex) {
                log.warn("handlePriceDownEvent exception occurred with productId: {}: {}",
                        productId, ex.getMessage(), ex);
            }

            @Override
            public void onSuccess(SendResult<String, PriceDownMessage> result) {

            }
        });
    }

    @Override
    public void close() throws Exception {
        eventBus.unregister(this);
    }
}
