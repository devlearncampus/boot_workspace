package com.sinse.bootwebsocket.controller;

import com.sinse.bootwebsocket.domain.Member;
import com.sinse.bootwebsocket.dto.ChatResponse;
import com.sinse.bootwebsocket.dto.CreateRoomResponse;
import com.sinse.bootwebsocket.dto.EnterRoomResponse;
import com.sinse.bootwebsocket.dto.Room;
import com.sinse.bootwebsocket.model.chat.RoomRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.UUID;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ChatStompController {

    private final SimpMessagingTemplate messagingTemplate;
    private final RoomRegistry registry;

    /** 접속 직후, 클라이언트가 방 목록/접속자 목록을 원한다면 별도 요청으로 만들어도 됨 */

    /** 1) 방 생성: 기존 requestType == "createRoom" */
    @MessageMapping("/room.create") // 클라이언트: /app/room.create 로 전송
    public void createRoom(CreateRoomRequest req, SimpMessageHeaderAccessor headers) {
        Member sessionMember = (Member) headers.getSessionAttributes().get("member");
        if (sessionMember == null) {
            // 인증 실패 응답을 사용자 큐로
            messagingTemplate.convertAndSendToUser(headers.getSessionId(), "/queue/errors",
                    "NOT_AUTHENTICATED");
            return;
        }

        // 웹소켓에 담긴 로그인 사용자와 요청의 userId 일치 여부 검사
        if (!sessionMember.getId().equals(req.getUserId())) {
            messagingTemplate.convertAndSendToUser(headers.getSessionId(), "/queue/errors",
                    "INVALID_USER");
            return;
        }

        // 방 생성
        Room room = new Room();
        room.setUUID(UUID.randomUUID().toString());
        room.setMaster(req.getUserId());
        room.setRoomName(req.getRoomName());

        // 방장 참여자로 등록(민감정보 배제)
        Member safe = new Member();
        safe.setId(sessionMember.getId());
        safe.setName(sessionMember.getName());
        safe.setEmail(sessionMember.getEmail());
        room.getUserList().add(safe);

        // 레지스트리 갱신
        registry.getRoomList().add(room);
        registry.getMemberList().add(safe);

        // 브로드캐스트: 모든 구독자에게 새로운 방 정보 통지
        CreateRoomResponse resp = new CreateRoomResponse();
        resp.setMemberList(registry.getMemberList());
        resp.setRoomList(registry.getRoomList());

        // 모든 클라이언트가 구독하는 토픽 (예: 방 목록용)
        messagingTemplate.convertAndSend("/topic/rooms", resp);
    }

    /** 2) 방 입장: 기존 requestType == "enterRoom" */
    @MessageMapping("/room.enter")
    public void enterRoom(EnterRoomRequest req, SimpMessageHeaderAccessor headers) {
        Member sessionMember = (Member) headers.getSessionAttributes().get("member");
        if (sessionMember == null) {
            messagingTemplate.convertAndSendToUser(headers.getSessionId(), "/queue/errors",
                    "NOT_AUTHENTICATED");
            return;
        }

        Room room = registry.findRoom(req.getUuid());
        if (room == null) {
            messagingTemplate.convertAndSendToUser(headers.getSessionId(), "/queue/errors",
                    "ROOM_NOT_FOUND");
            return;
        }

        boolean exists = room.getUserList().stream()
                .anyMatch(m -> m.getId().equals(sessionMember.getId()));

        if (!exists) {
            Member safe = new Member();
            safe.setId(sessionMember.getId());
            safe.setName(sessionMember.getName());
            safe.setEmail(sessionMember.getEmail());
            room.getUserList().add(safe);
            registry.getMemberList().add(safe);
        }

        EnterRoomResponse resp = new EnterRoomResponse();
        resp.setRoom(room);

        // 요청자에게 현재 방 상태 반환 (개별 큐 사용) — 필요하면 /topic/room.{uuid}로 브로드캐스트도 가능
        messagingTemplate.convertAndSendToUser(headers.getSessionId(), "/queue/room.enter", resp);

        // 선택: 같은 방 구독자에게 “누가 들어왔는지” 갱신 브로드캐스트
        messagingTemplate.convertAndSend("/topic/room." + room.getUUID(), resp);
    }

    /** 3) 채팅: 기존 requestType == "chat" */
    @MessageMapping("/chat.send")
    public void sendChat(ChatRequest req, SimpMessageHeaderAccessor headers) {
        Member sessionMember = (Member) headers.getSessionAttributes().get("member");
        if (sessionMember == null) {
            messagingTemplate.convertAndSendToUser(headers.getSessionId(), "/queue/errors",
                    "NOT_AUTHENTICATED");
            return;
        }

        Room room = registry.findRoom(req.getUuid());
        if (room == null) {
            messagingTemplate.convertAndSendToUser(headers.getSessionId(), "/queue/errors",
                    "ROOM_NOT_FOUND");
            return;
        }

        // 방 참여자인지 확인 (옵션)
        boolean inRoom = room.getUserList().stream()
                .anyMatch(m -> m.getId().equals(sessionMember.getId()));
        if (!inRoom) {
            messagingTemplate.convertAndSendToUser(headers.getSessionId(), "/queue/errors",
                    "NOT_IN_ROOM");
            return;
        }

        ChatResponse resp = new ChatResponse();
        resp.setUuid(req.getUuid());
        resp.setSender(req.getSender());
        resp.setData(req.getData());

        // 같은 방 구독자에게 브로드캐스트
        messagingTemplate.convertAndSend("/topic/room." + room.getUUID(), resp);
    }
}
