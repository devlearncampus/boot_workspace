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
public class HttpSessionToWsAttributesInterceptor implements HandshakeInterceptor {
    @Override
    public boolean beforeHandshake(ServerHttpRequest req, ServerHttpResponse res, WebSocketHandler h, Map<String, Object> attrs) {
        // 예: HttpSession에서 member 꺼내서 WS attributes에 저장
        if (req instanceof ServletServerHttpRequest s) {
            HttpSession httpSession = s.getServletRequest().getSession(false);
            if (httpSession != null) {
                attrs.put("member", httpSession.getAttribute("member"));
            }
        }
        return true;
    }
    @Override public void afterHandshake(ServerHttpRequest req, ServerHttpResponse res, WebSocketHandler h, Exception ex) {}
}