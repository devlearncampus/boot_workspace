package com.sinse.bootwebsocket.model.chat;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final ChatWebSocketHandler handler;
    private final HttpSessionInterceptor interceptor;

    //STOMP 를 사용하기 위한 설정
    private final HttpSessionToWsAttributesInterceptor httpSessionToWsAttributesInterceptor;

    public WebSocketConfig(ChatWebSocketHandler handler,HttpSessionInterceptor interceptor, HttpSessionToWsAttributesInterceptor httpSessionToWsAttributesInterceptor ) {
        this.handler = handler;
        this.interceptor = interceptor;
        this.httpSessionToWsAttributesInterceptor = httpSessionToWsAttributesInterceptor;
    }

    /*
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(handler, "/ws/chat")
                .addInterceptors(interceptor,httpSessionToWsAttributesInterceptor)
                .setAllowedOrigins("*");
    }
    */

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // 클라이언트 접속 엔드포인트 (ws://.../stomp-chat)
        registry.addEndpoint("/stomp-chat")
                .addInterceptors(httpSessionToWsAttributesInterceptor)
                .setAllowedOriginPatterns("*")
                .withSockJS(); // 필요시 제거 가능
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // 서버 → 클라이언트 브로드캐스트 대상(prefix)
        registry.enableSimpleBroker("/topic", "/queue");
        // 클라이언트 → 서버 앱 목적지(prefix)
        registry.setApplicationDestinationPrefixes("/app");
        // 사용자별 큐(prefix; convertAndSendToUser용)
        registry.setUserDestinationPrefix("/user");
    }
}