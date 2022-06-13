package com.daangndaangn.common.config;

import com.daangndaangn.common.chat.document.message.ChatMessageReadConverter;
import com.daangndaangn.common.chat.document.message.ChatMessageWriteConverter;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Slf4j
@Configuration
@RequiredArgsConstructor
@EnableMongoRepositories(
        basePackages = "com.daangndaangn.common.chat.repository"
)
public class MongoDbConfig extends AbstractMongoClientConfiguration {

    private final MongoConfig mongoConfig;

    @Bean(name = "mongoTransactionManager")
    public MongoTransactionManager mongoTransactionManager(MongoDatabaseFactory dbFactory) {
        return new MongoTransactionManager(dbFactory);
    }

    @Override
    protected String getDatabaseName() {
        return mongoConfig.getDatabase();
    }

    @Override
    protected void configureClientSettings(MongoClientSettings.Builder builder) {
        log.info("mongoConfigure.getUri(): {}", mongoConfig.getUri());
        log.info("mongoConfigure.getDatabase(): {}", mongoConfig.getDatabase());
        builder.applyConnectionString(new ConnectionString(mongoConfig.getUri()));
    }

    @Override
    protected void configureConverters(MongoCustomConversions.MongoConverterConfigurationAdapter adapter) {
        adapter.registerConverter(new ChatMessageReadConverter());
        adapter.registerConverter(new ChatMessageWriteConverter());
    }
}
