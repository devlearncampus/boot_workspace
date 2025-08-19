package com.sinse.chatroomapp.dto;

import com.sinse.chatroomapp.domain.Member;
import jakarta.websocket.Session;
import lombok.Data;

import java.util.Set;

@Data
public class Room {
    private String UUID;
    private String master; //방장
    private String roomName; //방제
    private Set<Member> users; //참여자 id 들

}
