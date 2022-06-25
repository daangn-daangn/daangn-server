package com.daangndaangn.apiserver.config;

import com.daangndaangn.common.message.BuyerReviewCreatedMessage;
import com.daangndaangn.common.message.PriceDownMessage;
import com.daangndaangn.common.message.SoldOutMessage;
import com.daangndaangn.common.message.SoldOutToBuyerMessage;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaProducerConfig {

    @Value("${spring.kafka.producer.bootstrap-servers}")
    private String bootstrapServers;

    @Bean
    @Qualifier("kafkaSoldOutTemplate")
    public KafkaTemplate<String, SoldOutMessage> kafkaSoldOutTemplate() {
        return new KafkaTemplate<>(soldOutProducerFactory());
    }

    @Bean
    public ProducerFactory<String, SoldOutMessage> soldOutProducerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfigs());
    }

    @Bean
    @Qualifier("kafkaPriceDownTemplate")
    public KafkaTemplate<String, PriceDownMessage> kafkaPriceDownTemplate() {
        return new KafkaTemplate<>(priceDownProducerFactory());
    }

    @Bean
    public ProducerFactory<String, PriceDownMessage> priceDownProducerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfigs());
    }

    @Bean
    @Qualifier("kafkaSoldOutToBuyerTemplate")
    public KafkaTemplate<String, SoldOutToBuyerMessage> kafkaSoldOutToBuyerTemplate() {
        return new KafkaTemplate<>(soldOutToBuyerProducerFactory());
    }

    @Bean
    public ProducerFactory<String, SoldOutToBuyerMessage> soldOutToBuyerProducerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfigs());
    }

    @Bean
    @Qualifier("kafkaBuyerReviewCreatedTemplate")
    public KafkaTemplate<String, BuyerReviewCreatedMessage> kafkaBuyerReviewCreatedTemplate() {
        return new KafkaTemplate<>(buyerReviewCreatedProducerFactory());
    }

    @Bean
    public ProducerFactory<String, BuyerReviewCreatedMessage> buyerReviewCreatedProducerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfigs());
    }

    private Map<String, Object> producerConfigs() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configs.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configs.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return configs;
    }
}
