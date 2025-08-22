package com.sinse.stompbasic.controller;

import com.sinse.stompbasic.dto.ChatMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Controller
public class ChatController {

    private Set<String> connectedUsers=new ConcurrentHashMap<>().newKeySet();

    /*클라이언트의 접속 요청 처리
     클라이언트가 7777/app/connect 로 접속하여 이 메서드 실행

     고전적인 방식의 웹소켓에서는 클라이언트가 전송한 프로토콜에 의해 if문을 사용했지만,
     STOMP 에서는 전통적인 방식이 아닌, 마치 웹요청을 처리하듯 클라이언트의 요청을 구분할 수 있음
     따라서 개발자가 별도의 프로토콜을 설계 할 필요가 없다
    */
    @MessageMapping("/connect")
    @SendTo("/topic/users") //이 메서드 실행의 결과는 내부적으로 ObjectMapper에 의한 JSON문자열이다
    //또한 이 메서드 실행결과를 대상 클라이언트를 개발자가 직접 반복문으로 브로드케스팅을 수행하는것이 아니라
    // /topic/users 채널을 구독한, 클라이언트 한테 자동으로 전송한다
    public Set<String> connect(ChatMessage chatMessage){
        log.debug(chatMessage.getSender()+" 의 접속 요청 받음");

        connectedUsers.add(chatMessage.getSender());
        return connectedUsers;
    }

}






