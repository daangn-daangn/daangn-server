package com.daangndaangn.common.configure;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class MongoClientConfiguration extends AbstractMongoClientConfiguration {

    private final MongoConfigure mongoConfigure;

    @Override
    protected String getDatabaseName() {
        return mongoConfigure.getDatabase();
    }

    @Override
    public MongoClient mongoClient() {
        log.info("mongodb:userName {}", mongoConfigure.getUserName());
        log.info("mongodb:password {}", mongoConfigure.getPassword());
        log.info("mongodb:host {}", mongoConfigure.getHost());
        log.info("mongodb:port {}", mongoConfigure.getPort());
        log.info("mongodb:database {}", mongoConfigure.getDatabase());

        String connectionUri = String.format("mongodb://%s:%s@%s:%d/%s",
                mongoConfigure.getUserName(),
                mongoConfigure.getPassword(),
                mongoConfigure.getHost(),
                mongoConfigure.getPort(),
                mongoConfigure.getDatabase());

        return MongoClients.create(connectionUri);
    }
}
