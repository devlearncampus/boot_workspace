package com.sinse.chatroomapp.dto;

import com.sinse.chatroomapp.domain.Member;
import lombok.Data;

import java.util.Set;

@Data
public class Room {
    private String UUID;//방의 고유 식별자
    private String master; //방장 아이디
    private String roomName; //방제목
    private Set<Member> userList;

}
