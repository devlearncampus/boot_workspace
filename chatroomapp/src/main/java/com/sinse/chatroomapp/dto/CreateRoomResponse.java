package com.sinse.chatroomapp.dto;

import com.sinse.chatroomapp.domain.Member;
import lombok.Data;

import java.util.Set;

/*
{
    responseType:"createRoom",
    memberList : [
        {
            id:"mario",
            name:"마리오",
            email:"zino1187@naver.com"
        }
    ],
    roomList:[
    ]
}
 */
@Data
public class CreateRoomResponse {
    private String responseType;
    private Set<Member> memberList;
    private Set<Room> roomList;

}