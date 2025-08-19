package com.sinse.chatroomapp.dto;

import jakarta.websocket.Session;
import lombok.Data;

import java.util.Set;

@Data
public class Room {
    private String UUID;
    private String master; //방장
    private String roomName; //방제
    private Set<String> users; //참여자 id 들

}
