package com.sinse.jwtlogin.util;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")  //application.properrites 파일에 등록된 키 가져오기
    private String secretkey;

    @Value("${jwt.expiration}")
    private long accessTokenExpiration; //유효기간

    @Value("${jwt.refresh.expiration}")
    private long refreshTokenExpiration; //유효기간

    //AccessToken 발급
    public String generateAccessToken(String username){
        return generateToken(username,accessTokenExpiration);
    }

    //RefreshToken 발급
    public String generateRefreshToken(String username){
        return generateToken(username,accessTokenExpiration);
    }



    //토큰 생성
    private String generateToken(String username, long exp){
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date()) //이 토큰의 발급 시점
                .setExpiration(new Date(System.currentTimeMillis() + exp))
                .signWith(Keys.hmacShaKeyFor(secretkey.getBytes()), SignatureAlgorithm.HS256)
                .compact();
    }

    //토큰 검증
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(secretkey.getBytes()))
                    .build()
                    .parseClaimsJws(token);
            return true;
        }catch(JwtException e){
            return false;
        }
    }

    //회원id 꺼내기
    public String getUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(secretkey.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}
