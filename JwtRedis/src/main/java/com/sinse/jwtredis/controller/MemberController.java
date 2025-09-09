package com.sinse.jwtredis.controller;

import com.sinse.jwtredis.controller.dto.MemberDTO;
import com.sinse.jwtredis.domain.Member;
import com.sinse.jwtredis.model.member.RegistService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class MemberController {

    private RegistService registService;

    public MemberController(RegistService registService) {
        this.registService = registService;
    }

    @PostMapping("/member/login")
    public ResponseEntity<?> regist(@RequestBody MemberDTO memberDTO) {
        log.debug( "regist member :"+memberDTO);

        registService.regist(memberDTO);

        return ResponseEntity.ok(null);
    }

}





