package com.sinse.customlogindb.controller;

import com.sinse.customlogindb.util.PasswordCreator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
public class MemberController {

    private final PasswordCreator passwordCreator;

    @GetMapping("/")
    public String index() {
        String pwd=passwordCreator.getCryptPassword("apple");
        log.debug("생성된 암호화 비번은 "+pwd);
        return "member/index";
    }

    //개발자 커스텀 로그인폼
    @GetMapping("/loginform")
    public String loginForm() {
        return "member/login";
    }

    @GetMapping("/main")
    public String getIndex(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        model.addAttribute("user", userDetails.getUsername());
        return "member/index";
    }
}






