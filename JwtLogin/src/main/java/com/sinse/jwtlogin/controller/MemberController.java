package com.sinse.jwtlogin.controller;

import com.sinse.jwtlogin.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Map;

@Slf4j
@Controller
public class MemberController {

    private final AuthenticationManager authencationManager;
    private final JwtUtil jwtUtil;

    public MemberController(AuthenticationManager authencationManager, JwtUtil jwtUtil) {
        this.authencationManager = authencationManager;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/member/login")
    public ResponseEntity<?> login(String username, String password) {
        log.debug("로그인 요청처리를 개발자가 정의한 컨트롤러에서 처리함");

        //사용자 인증에 성공이 되면, 토큰 발급
        //사용자의 아이디와 패스워드를 검증하는 시큐리티의 객체는 DaoAuthenticationProvider 이며,
        //이 객체가 검증을 시도하게 하려면, AuthenticationManager가 보유한 authencate() 메서드를
        //호출하면 된다..
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password);

        Authentication authentication =authencationManager.authenticate(token);

        //Access Token
        String accessToken = jwtUtil.generateAccessToken(authentication.getName());

        //Refresh Token
        String refreshToken = jwtUtil.generateRefreshToken(authentication.getName());

        return ResponseEntity.ok(Map.of("accessToken",accessToken,"refreshToken",refreshToken));
    }

    @GetMapping("/member/mypage")
    public ResponseEntity<?> mypage() {
        log.debug("회원의 mypage 접근 성공");
        return ResponseEntity.ok("접근 성공");
    }


}
