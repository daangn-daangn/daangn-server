package com.daangndaangn.chatserver.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    /**
     * SockJs Fallback을 이용해 노출할 STOMP endpoint를 설정하는 부분입니다.
     * sockJs 클라이언트가 Websocket 핸드셰이크를 하기 위해 연결할 endpoint를 지정할 수 있습니다.
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws-chat")
                .setAllowedOrigins("http://localhost:3000")
                .withSockJS();
    }

    /**
     * .enableSimpleBroker
     *     MessageBroker에 연결할 prefix를 적습니다.
     *     해당 설정을 적용하면, enableSimpleBroker에 추가된 엔드포인트 prefix를 달고 들어오는 요청은 브로커 채널에 전달됩니다.
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.setApplicationDestinationPrefixes("/app");
        registry.enableSimpleBroker("/topic");
    }
}
