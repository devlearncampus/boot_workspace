package com.sinse.broadcastcapp.dto;

import lombok.Data;

@Data
public class ResponseChat {
    private String responseType;
    private String sender;
    private String data;
}
