package com.sinse.jwtlogin.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;

import java.util.Date;
import java.util.UUID;

public class JwtUtil {

    @Value("${jwt.secret}")  //application.properrites 파일에 등록된 키 가져오기
    private String serectkey;

    //토큰 생성
    private String generateToken(String username, long exp){
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date()) //이 토큰의 발급 시점
                .setExpiration(new Date(System.currentTimeMillis() + exp))
                .signWith(Keys.hmacShaKeyFor(serectkey.getBytes()), SignatureAlgorithm.HS256)
                .compact();
    }
    
}
