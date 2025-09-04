package com.sinse.jwtlogin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
    //스프링 시큐리티의 기본으로 제공되는 폼로그인은 , 로그인 인증 성공 후 무조건
    //   / 로 리다이렉트 하기 때문에, /에 대한 요청을 처리하자
    @GetMapping("/")
    public String index() {
        return "index";
    }
}
