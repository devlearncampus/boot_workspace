package com.sinse.bootwebsocket.model.chat;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sinse.bootwebsocket.dto.ChatMessage;
import com.sinse.bootwebsocket.dto.ChatRoom;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/*
Spring에서 웹소켓 Server Endpoint 를 다루는 객체는 WebSocketHandler만 있는게 아님

[ TextWebSocketHandler ]
 - 다루고자 하는 데이터가 Text일 경우 효율적
 - 인터페이스가 아니므로, 사용되지도 않는 메서드를 재정의할 필요 없다. 즉 필요한 것만 골라서 재정의
*/

@Slf4j
@Component
public class ChatTextWebSocketHandler extends TextWebSocketHandler {

    //java - json 문자열과의 변환을 자동으로 처리해주는 객체
    private ObjectMapper objectMapper = new ObjectMapper();

    //현재 서버에 연결되어 있는 모든 클라이언트 세션집합 (클라이언트 전송용 아님)
    private Set<WebSocketSession> sessions=new ConcurrentHashMap<>().newKeySet();

    //현재 서버에 접속되어 있는 모든 클라이언트 아이디집합(클라이언트 전송용)
    private Set<String> connectedUsers=ConcurrentHashMap.newKeySet();

    //전체 방목록 집합
    private Map<String, ChatRoom> roomStorage=new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        //클라이언트가 접속 할때
        sessions.add(session);
    }

    //모든 클라이언트가 동시에 알아야할 정보를 전송할 브로드케스트 메서드 정의
    //매개변수가 Object  인 이유는? ObjectMapper에게 json 문자열로 변환을 맡길 데이터 형식이
    //Set, Map, 등등 일 수도 있기 때문에 결정되어 있지 않기 때문에...
    private void broadcast(String destination, Object data) throws Exception {
        String payload = objectMapper.writeValueAsString(Map.of("destination",destination,"body",data));

        //접속한 모든 클라이언트에게 전송
        for(WebSocketSession session: sessions){
            if(session.isOpen()){ //많은 세션 들중 닫히지 않은 상태의 세션..
                session.sendMessage(new TextMessage(payload));
            }
        }

    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        //클라이언트는 java가 이해할 수 없는 json 문자열 형태로 메시지를 전송하므로,
        //서버측에서는 해석이 필요하다..(parsing)
        //objectMapper.readValue(해석대상문자열, 어떤자바객체)

        ChatMessage chatMessage=objectMapper.readValue(message.getPayload(), ChatMessage.class);

        //클라이언트 요청 분기
        switch (chatMessage.getType()){

            case "CONNECT" ->{
                //주의: 클라이언트가 접속하자마자 메시지 전송을 보냈을때,
                //afterConnectionEstablished() 메서드에서 처리하는 것이아니라,
                //메시지를 받은 handleTextMessage() 메서드에서 처리한다..
                connectedUsers.add(chatMessage.getSender());
                broadcast("/users", connectedUsers);
            }
            case "DISCONNECT" ->{
                connectedUsers.remove(chatMessage.getSender());
                broadcast("/users", connectedUsers);
            }
            case "MESSAGE" ->{
                broadcast("/messages",chatMessage);
            }
            case "ROOM_CREATE" ->{
                ChatRoom room = new ChatRoom();

                String uuid= UUID.randomUUID().toString();
                room.setRoomId(uuid);
                room.setRoomName(chatMessage.getContent());
                //생성된 방을 방목록에 추가
                roomStorage.put(uuid, room);
                broadcast("/rooms", room);
            }
            case "ROOM_LIST" ->{
                broadcast("/rooms",roomStorage.values());
            }
            case "ROOM_ENTER" ->{
                //방을 검색하여, 검색된 방의 Set에 유저를 추가
                ChatRoom room = roomStorage.get(chatMessage.getRoomId());
                if (room != null) {
                    room.getUsers().add(chatMessage.getSender());
                }
                broadcast("/rooms", roomStorage.values());
            }
            case "ROOM_LEAVE" ->{
                // 특정 방에서 유저 제거
                ChatRoom room = roomStorage.get(chatMessage.getRoomId());
                if (room != null) {
                    room.getUsers().remove(chatMessage.getSender());
                }
                broadcast("/rooms", roomStorage.values());
            }

        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        super.afterConnectionClosed(session, status);
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        super.handleTransportError(session, exception);
    }
}















