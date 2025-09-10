package com.sinse.jwtredis.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

public class CookieUtil {
    //쿠키가 클라이언트 측에 저장되는 기술은 맞지만, 해당 쿠키를 생성하는 방법은 서버측에서
    //응답 정보로 포함시킬수도 있음 ..jsp/servlet에서도 쿠키를 클라이언트측에 생성하는 것이가능
    //예) 브라우저에게 어떤쿠키를 만들지 서버가 결정할 수 있다..
    //쿠기 생성
    //refreshtoken은 보안상 중요하므로, 클라이언트 측의 js가 절대로 코드로 접근할 수 없도록
    //httpOnly 속성을 true로 세팅해서 응답 정보로 보내자
    public static void setRefreshCookie(HttpServletResponse response, String token, int maxAgeSec){
        Cookie cookie = new Cookie("Refresh",token);
        cookie.setHttpOnly(true); //JS 접근 불가(보안)
        cookie.setSecure(false); // true 인 경우 HTTPS, 개발 시에만 꺼놓고 실제 운영에서는 보안인증서 설정
        cookie.setPath("/");    //클라이언트의 모든 경로에서 쿠키 사용가능
        cookie.setMaxAge(maxAgeSec); //유효기간(초)
        response.addCookie(cookie); //응답 시 쿠키로 전송
    }

    //쿠키 삭제
    public static void clearRefreshCookie(HttpServletResponse response){
        Cookie cookie = new Cookie("Refresh","");
        cookie.setHttpOnly(true); //JS 접근 불가(보안)
        cookie.setSecure(false); // true 인 경우 HTTPS, 개발 시에만 꺼놓고 실제 운영에서는 보안인증서 설정
        cookie.setPath("/");    //클라이언트의 모든 경로에서 쿠키 사용가능
        cookie.setMaxAge(0); //유효기간(초)
        response.addCookie(cookie); //응답 시 쿠키로 전송
    }
}
