package com.sinse.jwtapp.util;


import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

//BCrypt 알고리즘을 적용한 비밀번호 생성클래스
public class PasswordGenerator {
    public static String convert(String password){
        //매개변수로 전달된 평문 비밀번호를 BCrypt 알고리즘을 적용하여 변환함
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String result=encoder.encode(password);
        return result;
    }

    public static void main(String[] args) {
        System.out.println(PasswordGenerator.convert("1234"));
    }

}