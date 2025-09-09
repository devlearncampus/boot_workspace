package com.sinse.jwtredis.model.member;

import com.sinse.jwtredis.controller.dto.MemberDTO;
import com.sinse.jwtredis.domain.Member;
import org.springframework.stereotype.Service;

@Service
public interface RegistService {
    public void regist(MemberDTO memberDTO);
}
