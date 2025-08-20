package com.sinse.chatroomapp.dto;

import com.sinse.chatroomapp.domain.Member;
import lombok.Data;

import java.util.Set;

@Data
public class CloseResponse {
    private String responseType;
    private Set<Member> memberList;
    private Set<Room> roomList;

}
