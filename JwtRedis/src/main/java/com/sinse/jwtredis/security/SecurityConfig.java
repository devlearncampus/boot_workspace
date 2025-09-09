package com.sinse.jwtredis.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf( csrf -> csrf.disable())

                .authorizeHttpRequests( auth -> auth
                        .requestMatchers("/member/regist.html", "/member/login","/member/refresh" ,"/member/logout").permitAll()
                        .anyRequest().authenticated() //이외 요청은 로그인을 해야 통과..
                )
                .build();//JWT 기반이므로 CSRF 불필요
    }

}













