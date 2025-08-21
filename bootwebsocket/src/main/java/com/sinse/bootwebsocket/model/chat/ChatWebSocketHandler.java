package com.sinse.bootwebsocket.model.chat;

//javaee 순수 api로 ServerEndpoint를 구현했던 클래스와 같은 역할을 수행하는 클래스
//단, 스프링기반 api로 구현해본다

import jakarta.websocket.server.ServerEndpoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

@Slf4j
@Component
public class ChatWebSocketHandler implements WebSocketHandler {

    //javaee api의 @OnOpen과 동일
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        log.debug("새 클라이언트가 연결됨", session.getId());
    }

    //javaee api의 @OnMessage와 동일
    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        log.debug("클라이언트가 보낸 메시지 "+message.getPayload().toString());
    }

    //javaee api의 @OnError와 동일
    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {

    }

    //javaee api의 @OnClose와 동일
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {

    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
}
