package com.sinse.electroshop.controller.shop;

import com.sinse.electroshop.websocket.dto.ChatMessage;
import com.sinse.electroshop.websocket.dto.ChatRoom;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

/*
Spring 의 STOMP를 이용하면 웹소켓을 일반컨트롤러로 제어할 수 있다
마치 웹요청을 처리하듯...
*/
@Slf4j
@Controller
public class ChatController {

    //서버에 접속한 모든 유저 목록
    private Set<String> connectedUsrs=new ConcurrentHashMap<>().newKeySet();

    //서버에 존재하는 모든 방목록(상품의 수와 일치)
    private Map<String, ChatRoom> roomStorage=new ConcurrentHashMap<>();

    /*--------------------------------------------
    접속 요청 처리
    접속과 동시에 해당 상품과 관련된 방하나를 선택하고, 그 방에 참여한 고객목록을 반환
    --------------------------------------------*/
    @MessageMapping("/connect") //   localhost:9999/app/conntect
    public Set<String> connect(ChatMessage message) {
        log.debug("클라이언트 접속함");
        return null;
    }
}








