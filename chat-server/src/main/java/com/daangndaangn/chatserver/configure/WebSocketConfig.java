package com.daangndaangn.chatserver.configure;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws-chat")
                .setAllowedOrigins("http://localhost:3000")
                .withSockJS();
    }

    /**
     * .setApplicationDestinationPrefixes
     *      client에서 SEND 요청을 처리합니다.
     *      클라이언트가 발행하는 요청 (publish)을 처리하는 prefix 설정
     *
     * .enableSimpleBroker
     *      해당 경로로 SimpleBroker를 등록합니다.
     *      SimpleBroker는 해당하는 경로를 SUBSCRIBE하는 client에게 메시지를 전달하는 간단한 작업을 수행
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.setApplicationDestinationPrefixes("/app");
        registry.enableSimpleBroker("/topic");
    }
}
