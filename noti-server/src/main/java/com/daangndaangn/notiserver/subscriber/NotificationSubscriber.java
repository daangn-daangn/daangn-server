package com.daangndaangn.notiserver.subscriber;

import com.daangndaangn.common.message.BuyerReviewCreatedMessage;
import com.daangndaangn.common.message.PriceDownMessage;
import com.daangndaangn.common.message.SoldOutMessage;
import com.daangndaangn.common.message.SoldOutToBuyerMessage;
import com.daangndaangn.notiserver.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * api-server로부터 들어오는 event를 받는 eventListener
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class NotificationSubscriber {

    private final NotificationService notificationService;

    @KafkaListener(topics = "${spring.kafka.topic.sold-out}",
        containerFactory = "kafkaListenerContainerSoldOutFactory")
    public void handleSoldOutMessage(SoldOutMessage message) {
        log.info("NotificationSubscriber::{}", message);

        notificationService.createFavoriteProductNotification(message.getUserIds(),
                                    message.getNotificationType(),
                                    message.getProductId()
        );
    }

    @KafkaListener(topics = "${spring.kafka.topic.price-down}",
            containerFactory = "kafkaListenerContainerPriceDownFactory")
    public void handlePriceDownMessage(PriceDownMessage message) {
        log.info("NotificationSubscriber::{}", message);

        notificationService.createFavoriteProductNotification(message.getUserIds(),
                                    message.getNotificationType(),
                                    message.getProductId()
        );
    }

    @KafkaListener(topics = "${spring.kafka.topic.sold-out-to-buyer}",
            containerFactory = "kafkaListenerContainerSoldOutToBuyerFactory")
    public void handleSoldOutToBuyerMessage(SoldOutToBuyerMessage message) {
        log.info("NotificationSubscriber::{}", message);

        notificationService.createSoldOutToBuyerNotification(message.getBuyerId(),
                                    message.getNotificationType(),
                                    message.getSellerId(),
                                    message.getProductId()
        );
    }

    @KafkaListener(topics = "${spring.kafka.topic.buyer-review-created}",
            containerFactory = "kafkaListenerContainerBuyerReviewCreatedFactory")
    public void handleBuyerReviewCreatedMessage(BuyerReviewCreatedMessage message) {
        log.info("NotificationSubscriber::{}", message);

        notificationService.createBuyerReviewCreatedNotification(message.getSellerId(),
                                    message.getNotificationType(),
                                    message.getReviewerId()
        );
    }
}
