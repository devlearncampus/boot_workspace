package com.sinse.bootwebsocket.model.chat;

import com.sinse.bootwebsocket.domain.Member;
import com.sinse.bootwebsocket.dto.Room;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RoomRegistry {
    private final Set<Member> memberList = ConcurrentHashMap.newKeySet();
    private final Set<Room> roomList = ConcurrentHashMap.newKeySet();

    public Set<Member> getMemberList() { return memberList; }
    public Set<Room> getRoomList() { return roomList; }

    public Room findRoom(String uuid) {
        for (Room r : roomList) {
            if (uuid.equals(r.getUUID())) return r;
        }
        return null;
    }
}