package com.sinse.bootwebsocket.dto;

import lombok.Data;

@Data
public class ChatResponse {
    private String responseType;
    private String sender;
    private String data;
    private String uuid;

}
