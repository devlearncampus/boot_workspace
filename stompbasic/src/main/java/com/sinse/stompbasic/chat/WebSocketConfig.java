package com.sinse.stompbasic.chat;


import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        //서버가 클라이언트에게 메세지를 브로드케스팅할때 사용할 접두어(채널구분)
        registry.enableSimpleBroker("/topic");

        //클라이언트에서 서버로 요청을 보낼때는 무조건 접두어에 /app
        //마치 스프링 레거시에서 web.xml에 context root path를 /admin, /shop
        registry.setApplicationDestinationPrefixes("/app");

        //브로드케스팅이 아닌 1:1 메시징 처리에서 사용할 사용자의 prefix
        //클라이언트는 무조건 /user/.../ 처럼 /user로 시작
        registry.setUserDestinationPrefix("/user");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        //클라이언트의 서버 접속 엔드포인트 지정
        registry.addEndpoint("/ws") //웹소켓 엔드포인트
                .setAllowedOriginPatterns("*");
    }
}





