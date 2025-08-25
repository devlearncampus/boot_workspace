package com.sinse.stompbasic.controller;

import com.sinse.stompbasic.dto.ChatMessage;
import com.sinse.stompbasic.dto.ChatRoom;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Controller
public class ChatController {

    //서버에 접속한 유저 목록
    private Set<String> connectedUsers=new ConcurrentHashMap<>().newKeySet();

    //서버에 생성된 방목록
    private Map<String, ChatRoom> roomStorage=new ConcurrentHashMap<>();


    /*favicon 처리
    * 1) static 디렉토리에 실제로 favicon 이미지를 보유
    * 2) 컨트롤러에 요청을 처리하되, 이미지를 반환하지 않고 void
    * */
    @GetMapping("favicon.ico")
    @ResponseBody
    public void favicon(){
    }


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

    //접속 해제 메시지 요청 처리
    @MessageMapping("/disconnect")
    @SendTo("/topic/users")
    public Set<String> disconnect(ChatMessage chatMessage){
        connectedUsers.remove(chatMessage.getSender());
        return connectedUsers;
    }


    //클라이언트의 메시지 전송 처리
    @MessageMapping("/chat.send")
    @SendTo("/topic/messages") //  /topic/messages 를 구독한 클라이언트들에게 브로드케스팅
    public ChatMessage send(ChatMessage chatMessage){
        return chatMessage;
    }

    //방 만들기 메시지 전송 처리
    @MessageMapping("/room.create")
    @SendTo("/topic/rooms") //이 채널을 구독한 클라이언트에게 결과를 응답정보로 보내기
    public Collection<ChatRoom> createRoom(ChatMessage chatMessage){

        ChatRoom chatRoom=new ChatRoom();
        String roomId= UUID.randomUUID().toString();
        chatRoom.setRoomId(roomId); //고유ID
        chatRoom.setRoomName(chatMessage.getContent());//방이름

        //생성된 방을, 목록에 추가
        roomStorage.put(roomId, chatRoom);

        return roomStorage.values();
    }

    //방 참여 요청 메시지 처리
    @MessageMapping("/room.enter")
    @SendTo("/topic/rooms")
    public Collection<ChatRoom> enterRoom(ChatMessage chatMessage){
        //방을 검색하여, 발견된 방의 Set에 유저명넣기
        ChatRoom chatRoom = roomStorage.get(chatMessage.getRoomId());

        if(chatRoom != null){ //방이 존재한다면,
            chatRoom.getUsers().add(chatMessage.getSender());
        }

        return roomStorage.values();
    }

    //방나가기 요청 메시지 처리
    @MessageMapping("/room.leave")
    @SendTo("/topic/rooms")
    public Collection<ChatRoom> leaveRoom(ChatMessage chatMessage){
        //서버의 방 목록을 하나를 찾아내서, 그 안의 Set 유저정보에서 sender 제거
        ChatRoom room=roomStorage.get(chatMessage.getRoomId());

        if(room != null){
            room.getUsers().remove(chatMessage.getSender());
        }
        return roomStorage.values();
    }

    //방목록 요청 메시지 처리
    @MessageMapping("/room.list")
    @SendTo("/topic/rooms")
    public Collection<ChatRoom> listRoom(ChatMessage chatMessage){
        return roomStorage.values();
    }

}






