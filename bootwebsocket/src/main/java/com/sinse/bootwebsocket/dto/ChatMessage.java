package com.sinse.bootwebsocket.dto;

import lombok.Data;

//서버와 클라이언트가 대화를 위한 메시지 전달 객체
@Data
public class ChatMessage {
    /*
        CONNECT : 접속
        DISCONNECT : 접속 해제
        MESSAGE : 채팅
        ROOM_CREATE : 방만들기
        ROOM_LIST : 방목록
        ROOM_ENTER : 방 입장
        ROOM_LEAVE : 방 나오기
    */
    private String type;
    private String sender; //보낸 사람
    private String content; //메시지 내용
    private String roomId;//방 ID
}
