package com.sinse.bootwebsocket.dto;

import lombok.Data;

@Data
public class EnterRoomResponse {
    private String responseType;
    private Room room;
}
