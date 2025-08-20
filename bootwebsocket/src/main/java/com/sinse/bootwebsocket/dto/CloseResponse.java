package com.sinse.bootwebsocket.dto;


import com.sinse.bootwebsocket.domain.Member;
import lombok.Data;

import java.util.Set;

@Data
public class CloseResponse {
    private String responseType;
    private Set<Member> memberList;
    private Set<Room> roomList;

}
