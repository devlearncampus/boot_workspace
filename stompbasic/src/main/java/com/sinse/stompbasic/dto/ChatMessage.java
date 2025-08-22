package com.sinse.stompbasic.dto;

import lombok.Data;

@Data
public class ChatMessage {
    private String type;
    private String sender;
    private String content;
    private String roomId;
}
