package com.daangndaangn.apiserver.config;

import com.daangndaangn.apiserver.eventlistener.*;
import com.daangndaangn.apiserver.service.favorite.FavoriteProductService;
import com.daangndaangn.apiserver.service.product.views.ProductViewService;
import com.daangndaangn.common.event.EventExceptionHandler;
import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.TaskExecutor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.concurrent.ThreadPoolExecutor;

@Setter
@Getter
@Component
@ConfigurationProperties(prefix = "eventbus.thread")
public class EventConfig {

    private String threadNamePrefix;
    private int corePoolSize;
    private int maxPoolSize;
    private int queueCapacity;

    @Bean(name = "eventbus-executor")
    public TaskExecutor eventTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setThreadNamePrefix(threadNamePrefix);
        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setQueueCapacity(queueCapacity);
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setAwaitTerminationSeconds(60);
        executor.afterPropertiesSet();
        return executor;
    }

    @Bean
    public EventExceptionHandler eventExceptionHandler() {
        return new EventExceptionHandler();
    }

    @Bean
    public EventBus eventBus(TaskExecutor eventTaskExecutor, EventExceptionHandler eventExceptionHandler) {
        return new AsyncEventBus(eventTaskExecutor, eventExceptionHandler);
    }

    @Bean(destroyMethod = "close")
    public SoldOutEventListener soldOutEventListener(EventBus eventBus,
                                                     @Qualifier("kafkaSoldOutTemplate") KafkaTemplate kafkaTemplate,
                                                     FavoriteProductService favoriteProductService) {

        return new SoldOutEventListener(eventBus, kafkaTemplate, favoriteProductService);
    }

    @Bean(destroyMethod = "close")
    public PriceDownEventListener priceDownEventListener(EventBus eventBus,
                                                     @Qualifier("kafkaPriceDownTemplate") KafkaTemplate kafkaTemplate,
                                                     FavoriteProductService favoriteProductService) {

        return new PriceDownEventListener(eventBus, kafkaTemplate, favoriteProductService);
    }

    @Bean(destroyMethod = "close")
    public SoldOutToBuyerEventListener soldOutToBuyerEventListener(EventBus eventBus,
                                                                   @Qualifier("kafkaSoldOutToBuyerTemplate")
                                                                   KafkaTemplate kafkaTemplate) {

        return new SoldOutToBuyerEventListener(eventBus, kafkaTemplate);
    }

    @Bean(destroyMethod = "close")
    public BuyerReviewCreatedEventListener buyerReviewCreatedEventListener(EventBus eventBus,
                                                                           @Qualifier("kafkaBuyerReviewCreatedTemplate")
                                                                           KafkaTemplate kafkaTemplate) {

        return new BuyerReviewCreatedEventListener(eventBus, kafkaTemplate);
    }

    @Bean(destroyMethod = "close")
    public ProductViewEventListener productViewEventListener(EventBus eventBus,
                                                             ProductViewService productViewService) {

        return new ProductViewEventListener(eventBus, productViewService);
    }
}
