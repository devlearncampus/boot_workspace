package com.sinse.bootwebsocket.controller;

import com.sinse.bootwebsocket.domain.Member;
import com.sinse.bootwebsocket.model.member.MemberService;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@Controller
public class MemberController {

    private MemberService memberService;
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/member/loginform")
    public String loginForm() {

        return "member/login";
    }

    @PostMapping("/member/login")
    @ResponseBody
    public ResponseEntity login(Member member, HttpSession session) {

        log.debug(member.getId());
        log.debug(member.getPassword());

        Member obj=memberService.login(member);

        session.setAttribute("member", obj);

        return ResponseEntity.ok("success");
    }

    @GetMapping("/chat/main")
    public String main(HttpSession session) {
        String viewName="chat/main";

        if(session.getAttribute("member") == null) {
            viewName= "templates/member/login";
        }
        return viewName;
    }

    @GetMapping("/chat/room")
    public String room() {

        return "chat/room";
    }

}