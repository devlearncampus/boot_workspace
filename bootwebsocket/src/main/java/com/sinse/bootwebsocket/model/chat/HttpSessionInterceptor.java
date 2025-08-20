package com.sinse.bootwebsocket.model.chat;

import jakarta.servlet.http.HttpSession;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

@Component
public class HttpSessionInterceptor implements HandshakeInterceptor {

    //클라이언트가 WebSocket 연결을시도하면, HTTP 핸드세이크 요청이 발생하고, 이때 아래의 메서드가 호출됨
    //여기서 기존 HttpSession 을 꺼내고, 그 안에 member 객체를 꺼내서 WebSocketSession 의 attributes에 옮겨 저장
    @Override
    public boolean beforeHandshake(ServerHttpRequest request,ServerHttpResponse response,WebSocketHandler wsHandler, Map<String, Object> attributes) {
        if (request instanceof ServletServerHttpRequest servletRequest) {

            HttpSession httpSession = servletRequest.getServletRequest().getSession(false);

            if (httpSession != null) {
                //attributes 를 이용하여 데이터를 넣어두면, 나중에  WebSocketSession이 getAttributes() 로 접근 가능함
                attributes.put("HTTP_SESSION", httpSession);
                attributes.put("member", httpSession.getAttribute("member")); // 예: 로그인 객체
            }
        }
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,Exception ex) { }
}
