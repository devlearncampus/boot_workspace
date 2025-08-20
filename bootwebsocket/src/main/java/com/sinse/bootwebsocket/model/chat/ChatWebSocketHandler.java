package com.sinse.bootwebsocket.model.chat;

import com.sinse.bootwebsocket.domain.Member;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Slf4j
@Component
public class ChatWebSocketHandler extends TextWebSocketHandler {
    //@OnOpen 대체
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        log.debug("접속됨");

        HttpSession httpSession=(HttpSession)session.getAttributes().get("HTTP_SESSION");
        Member member = (Member)session.getAttributes().get("member");
        log.debug("회원 이름은 "+member.getName());

    }

    //@OnMessage 대체
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        log.debug("메시지 받음");

    }

    //@OnClose 대체
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        log.debug("접속 종료");
    }

    //@OnError 대체
    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        log.debug("에러발생");
    }
}
