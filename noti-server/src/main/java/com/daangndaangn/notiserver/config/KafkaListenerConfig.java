package com.daangndaangn.notiserver.config;

import com.daangndaangn.common.message.BuyerReviewCreatedMessage;
import com.daangndaangn.common.message.PriceDownMessage;
import com.daangndaangn.common.message.SoldOutMessage;
import com.daangndaangn.common.message.SoldOutToBuyerMessage;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaListenerConfig {

    @Value("${spring.kafka.consumer.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${spring.kafka.consumer.group-id}")
    private String groupId;

    @Value("${spring.kafka.consumer.auto-offset-reset}")
    private String offsetReset;

    @Bean
    public ConsumerFactory<String, SoldOutMessage> soldOutConsumerFactory() {
        return new DefaultKafkaConsumerFactory<>(
                consumerConfigs(),
                new StringDeserializer(),
                new JsonDeserializer<>(SoldOutMessage.class)
        );
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, SoldOutMessage> kafkaListenerContainerSoldOutFactory() {
        ConcurrentKafkaListenerContainerFactory<String, SoldOutMessage> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(soldOutConsumerFactory());
        return factory;
    }

    @Bean
    public ConsumerFactory<String, PriceDownMessage> priceDownConsumerFactory() {
        return new DefaultKafkaConsumerFactory<>(
                consumerConfigs(),
                new StringDeserializer(),
                new JsonDeserializer<>(PriceDownMessage.class)
        );
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, PriceDownMessage> kafkaListenerContainerPriceDownFactory() {
        ConcurrentKafkaListenerContainerFactory<String, PriceDownMessage> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(priceDownConsumerFactory());
        return factory;
    }

    @Bean
    public ConsumerFactory<String, SoldOutToBuyerMessage> soldOutToBuyerConsumerFactory() {
        return new DefaultKafkaConsumerFactory<>(
                consumerConfigs(),
                new StringDeserializer(),
                new JsonDeserializer<>(SoldOutToBuyerMessage.class)
        );
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, SoldOutToBuyerMessage> kafkaListenerContainerSoldOutToBuyerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, SoldOutToBuyerMessage> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(soldOutToBuyerConsumerFactory());
        return factory;
    }

    @Bean
    public ConsumerFactory<String, BuyerReviewCreatedMessage> buyerReviewCreatedConsumerFactory() {
        return new DefaultKafkaConsumerFactory<>(
                consumerConfigs(),
                new StringDeserializer(),
                new JsonDeserializer<>(BuyerReviewCreatedMessage.class)
        );
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, BuyerReviewCreatedMessage> kafkaListenerContainerBuyerReviewCreatedFactory() {
        ConcurrentKafkaListenerContainerFactory<String, BuyerReviewCreatedMessage> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(buyerReviewCreatedConsumerFactory());
        return factory;
    }

    private Map<String, Object> consumerConfigs() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configs.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        configs.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, offsetReset);
        return configs;
    }
}
