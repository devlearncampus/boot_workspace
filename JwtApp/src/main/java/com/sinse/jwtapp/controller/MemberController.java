package com.sinse.jwtapp.controller;

import com.sinse.jwtapp.domain.CustomUserDetails;
import com.sinse.jwtapp.model.member.CustomUserDetailsService;
import com.sinse.jwtapp.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@Slf4j
@Controller
public class MemberController {

    private final AuthenticationManager authManager;
    private final JwtUtil jwtUtil;

    public MemberController(AuthenticationManager authManager, JwtUtil jwtUtil) {
        this.authManager = authManager;
        this.jwtUtil = jwtUtil;
    }


    @PostMapping("/member/login")
    public ResponseEntity<?> login(String username, String password) {
        // authenticate() 메서드가 호출되는 순간 여기서 DaoAuthenticationProvider가 자동으로 호출되어
        // UDS 조회 + PasswordEncoder.matches() 실행됨
        var auth = authManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

        var access  = jwtUtil.generateAccessToken(auth.getName());
        var refresh = jwtUtil.generateRefreshToken(auth.getName());
        return ResponseEntity.ok(Map.of("accessToken", access, "user", auth.getName()));
    }
}
