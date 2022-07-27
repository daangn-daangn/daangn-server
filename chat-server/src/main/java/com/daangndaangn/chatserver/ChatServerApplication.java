package com.daangndaangn.chatserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

@EnableMongoAuditing
@SpringBootApplication(scanBasePackages = {"com.daangndaangn.common", "com.daangndaangn.chatserver"})
public class ChatServerApplication {

    @PostConstruct
    void started() {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
    }

    public static void main(String[] args) {
        SpringApplication.run(ChatServerApplication.class, args);
    }

}
