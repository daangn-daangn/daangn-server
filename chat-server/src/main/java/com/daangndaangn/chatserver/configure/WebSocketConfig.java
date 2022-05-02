package com.daangndaangn.chatserver.configure;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker //메세지 브로커를 사용하겠다는 어노테이션
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        /*
        소켓 연결을 할 때 사용되는 주소입니다.
        http://localhost:9080/chat
         */

        registry.addEndpoint("/chat").setAllowedOriginPatterns("*").withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        /*
        pub/sub 패턴에서 브로커 역할을 담당하는 주소입니다.
        "/queue", "/topic" 이 두 경로가 prefix(api 경로 맨 앞)에 붙은 경우,
        messageBroker가 잡아서 해당 채팅방을 구독하고 있는 클라이언트에게 메시지를 전달해줌

        "/queue", "/topic"은 임의의 경로이며 변경할 수 있습니다.
         */
        config.enableSimpleBroker("/topic", "/queue");
    }
}