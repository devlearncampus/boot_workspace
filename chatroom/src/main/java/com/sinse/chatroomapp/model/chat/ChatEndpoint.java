package com.sinse.chatroomapp.model.chat;


import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sinse.chatroomapp.domain.Member;
import com.sinse.chatroomapp.dto.Room;
import com.sinse.chatroomapp.dto.RoomResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.websocket.EndpointConfig;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
@ServerEndpoint(value = "/chat/multi", configurator =  HttpSessionConfigurator.class)
public class ChatEndpoint {

    //접속자 명단을 만든다
    private static Set<Session> userList = new HashSet<>();
    private static Set<Member> memberList = new HashSet<>(); //클라이언트에게 보내기 위한 정보
    private static Set<Room> roomList= new HashSet<>(); // key-UUID, value-유저목록

    private static ObjectMapper objectMapper = new ObjectMapper();

    @OnOpen
    public void onOpen(Session session, EndpointConfig config) throws Exception{
        log.debug("onOpen called");

        HttpSession httpSession=(HttpSession)config.getUserProperties().get(HttpSession.class.getName());
        log.debug("httpSession:"+httpSession);

        if(httpSession!=null){
            Member member =(Member)httpSession.getAttribute("member");
            log.debug("웹소켓으로 접속한 회원은 "+member.getId());

            session.getUserProperties().put("member",member);//웹소켓 세션에 회원아이디 심기
            userList.add(session);//접속자 목록에 추가하기

            //방개설 정보를 응답 정보로 보내기
            Member obj = new Member();
            obj.setId(member.getId());
            obj.setName(member.getName());
            obj.setEmail(member.getEmail());
            memberList.add(obj);
            /*
            * {
            *   responseType:"createRoom",
            *   memberList :[
            *       {
            *           id,name,email
            *       },
             *       {
             *           id,name,email
             *       }
            *   ]
            * }
            * */
            RoomResponse  roomResponse=new RoomResponse();
            roomResponse.setResponseType("createRoom");
            roomResponse.setMemberList(memberList);
            String json=objectMapper.writeValueAsString(roomResponse);
            log.debug(json);
            session.getAsyncRemote().sendText(json);
        }
    }

    @OnMessage
    public void onMessage(Session session, String message) throws Exception {
        JsonNode jsonNode =objectMapper.readTree(message);
        String requestType = jsonNode.get("requestType").asText();

        /*----------------------------------------------------------
        방 개성을 원하면
        ----------------------------------------------------------*/
        if(requestType.equals("createRoom")){
            log.debug("방 만들어줄께");

            String userId=jsonNode.get("userId").asText();
            String roomName=(String)session.getUserProperties().get("roomName");

            Member member=(Member)session.getUserProperties().get("member");

            if(!userId.equals(member.getId())){
                //방실패 메세지 전송
            }else{
                //방 만들기
                UUID uuid = UUID.randomUUID();
                Room room = new Room();
                room.setUUID(uuid.toString());
                room.setMaster(userId); //방장
                room.setRoomName(roomName);

                //방 참여자 등록
                Set users=new HashSet<>();

                Member obj=new Member();
                obj.setId(member.getId());
                obj.setName(member.getName());
                obj.setEmail(member.getEmail());

                users.add(obj);//방을 개설한 주인을 참여자로 등록
                room.setUsers(users);

                roomList.add(room);


                /*
                * 클라이언트에게 전송할 응답 프로토콜
                 {
                    responseType:"createRoom",
                    memberList:[
                        {
                        }
                    ],
                    roomList :  [
                        {
                            UUID: "dhfuwidfysadjkhfdsakj"
                            master:"mario",
                        }
                    ]
                 }
                */
                RoomResponse roomResponse=new RoomResponse();
                roomResponse.setResponseType("createRoom");
                roomResponse.setMemberList(memberList);
                roomResponse.setRoomList(roomList);


                session.getAsyncRemote().sendText(objectMapper.writeValueAsString(roomResponse));
            }

        }else if(requestType.equals("joinRoom")){
            log.debug("방에 들어가게 해줄께");
        }else if(requestType.equals("leaveRoom")){
            log.debug("방에서 나오게 해줄께");
        }else if(requestType.equals("sendMessage")){
            log.debug("채팅 메세지 보내줄께");
        }
    }

}
