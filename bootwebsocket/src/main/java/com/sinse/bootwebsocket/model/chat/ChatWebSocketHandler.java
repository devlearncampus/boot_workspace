package com.sinse.bootwebsocket.model.chat;

//javaee 순수 api로 ServerEndpoint를 구현했던 클래스와 같은 역할을 수행하는 클래스
//단, 스프링기반 api로 구현해본다

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sinse.bootwebsocket.dto.ChatMessage;
import com.sinse.bootwebsocket.dto.ChatRoom;
import jakarta.websocket.server.ServerEndpoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class ChatWebSocketHandler implements WebSocketHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();

    //총 접속자 목록
    //아래의 객체를 만일 ObjectMapper에게 변환을 맡기면 보안상 중요한 세션 정보가 json으로 전달되어버린다..
    //따라서 클라이언트는 접속자 아이디만 알면 되므로, connectedUsers라는 Set 별도로 정의하여 관리해야 한다
    private final Set<WebSocketSession> sessions=new ConcurrentHashMap<>().newKeySet();

    //다중 쓰레드 환경에서 동시성 문제를 이미 해결해놓은 ConcurrentHashMap 을 이용하여 Set 을 만든다
    private final Set<String> connectedUsers=new ConcurrentHashMap<>().newKeySet();

    //방 목록 저장소
    private final Map<String, ChatRoom> roomStorage=new ConcurrentHashMap<>();

    //javaee api의 @OnOpen과 동일
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        log.debug("새 클라이언트가 연결됨", session.getId());

        sessions.add(session); //실질적인 접속자 추가
    }

    //거의 모든 클라이언트의 요청마다, 서버는 접속한 클라이언트들을 대상으로 메시지를 전송해야 하므로
    //반복문을 수시로 Session 수만큼 돌려야 한다..따라서 공통적인 코드 이므로, 아예 메서드로 정의한다
    //또한 이 메서드는 handleMessage() 멤버 메서드에서만 접근할 것이므로, 굳이 public 외부에 공개할 필요가없다
    private void broadCast(String destination, Object data) throws Exception {
        String json=objectMapper.writeValueAsString(
                Map.of("destination",destination, "body", data)
        );
        //서버에 현재 접속해있는 모든 클라이언트의 세션만큼..반복..
        for(WebSocketSession session : sessions){
            if(session.isOpen()) {
                session.sendMessage(new TextMessage(json));
            }
        }
    }


    //javaee api의 @OnMessage와 동일
    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        log.debug("클라이언트가 보낸 메시지 "+message.getPayload().toString());

        //클라이언트가 보낸 메시지는 그냥 String 일뿐이므로, 분석을 위해서는 자바의 객체화시켜야 한다..
        //특히 클라이언트가 보낸 String이 json 문자열이므로, 라이브러리중 json 문자열과 java 객체간 변환을 자동으로
        //처리해주는 jackson 라이브러리 이용하자
        ChatMessage chatMessage=objectMapper.readValue(message.getPayload().toString(), ChatMessage.class);



        switch (chatMessage.getType()) {

            //새로운 유저가 접속했으므로, 총 접속자 목록에 추가
            case "CONNECT" ->{
                connectedUsers.add(chatMessage.getSender());
                //모든 접속된 유저들에게 접속자목록에 대한 브로드 케스팅
                //브로드케스팅 시, 클라이언트가 서버가 보낸 메시지를 구분할 수 있는 구분 채널, 또는
                //구분 값을 포함해서 보내주자
                broadCast("/users",connectedUsers);
            }
            case "DISCONNECT" ->{
                connectedUsers.remove(chatMessage.getSender());
                broadCast("/users", connectedUsers);
            }
            case "MESSAGE" ->{
                broadCast("/messages",chatMessage);
            }
            case "ROOM_CREATE" ->{
                //방을 생성
                String uuid=UUID.randomUUID().toString();
                ChatRoom room=new ChatRoom();
                room.setRoomId(uuid); //UUID
                room.setRoomName(chatMessage.getContent()); //roomName

                roomStorage.put(uuid, room);
                broadCast("/rooms",roomStorage.values());
            }
            case "ROOM_ENTER" ->{
                //Map에 모여있는 룸들 중 ,클라이언트가 참여하기를 원하는 룸을 검색하자
                ChatRoom room = roomStorage.get(chatMessage.getRoomId());
                if(room!=null){ //검색된 방이 발견된다면..
                    room.getUsers().add(chatMessage.getSender());//참여자로 등록
                }
                broadCast("/rooms",roomStorage.values());
            }
            case "ROOM_LEAVE" ->{
                ChatRoom room = roomStorage.get(chatMessage.getRoomId());

                if(room!=null){
                    room.getUsers().remove(chatMessage.getSender()); //룸에서 제거
                }
                broadCast("/rooms",roomStorage.values());
            }

        }




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
