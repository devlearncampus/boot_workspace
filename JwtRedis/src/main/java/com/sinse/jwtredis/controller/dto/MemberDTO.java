package com.sinse.jwtredis.controller.dto;


import lombok.Data;

@Data
public class MemberDTO {
    private int member_id;
    private String id;
    private String pwd;
    private String name;
    private String email;
    private String code; //6자리 랜덤값
    private String deviceId; //유저가 사용중인 디바이스의 고유값(디바이스마다 틀려야 함)
}
