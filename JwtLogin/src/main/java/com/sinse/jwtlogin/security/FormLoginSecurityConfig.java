package com.sinse.jwtlogin.security;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.sinse.jwtlogin.model.member.CustomUserDetailsService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Map;

//스프링 시큐리티를 개발자가 원하는 설정으로 변경하고자할때 이 클래스 정의가 필요함
//@Configuration
//@EnableWebSecurity
public class FormLoginSecurityConfig {

    //시큐리티가 사용할 서비스 객체 등록
    private final CustomUserDetailsService customUserDetailsService;

    public FormLoginSecurityConfig(CustomUserDetailsService customUserDetailsService) {
        this.customUserDetailsService = customUserDetailsService;
    }

    //DaoAuthenticationProvider가 사용할 비번 검증 인코더 등록
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); //비번결과물 자체에 salt 가 포함됨
    }

    //아이디와 패스워드를 자동으로 비교해주는 프로바이더 등록
    @Bean
    public DaoAuthenticationProvider daothenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        //프로바이더가 사용할 서비스 객체 등록
        provider.setUserDetailsService(customUserDetailsService);

        //프로바이더가 사용할 비번 인코더
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                //스프링시큐리티가 기본적으로 CRSF 방지기능이 있음 , 아래의 코드로 disable  시키면 
                //CSRF를 비활성화시킴 - 전통적인 form 로그인/세션 방식이 아니라 REST/JWT 로 인증을 처리할때는
                //보통 끔
                .csrf(AbstractHttpConfigurer::disable)

                .formLogin(form -> form
                        .loginPage("/member/login.html") //로그인 폼의 접근 url
                        .loginProcessingUrl("/member/login") //시큐리티가 로그인일 처리할 url

                        //동기 방식으로 요청이 들어올때, 결과를 html 보여줄때 아래의 코드사용
                        //.defaultSuccessUrl("/index.html") //로그인 성공후 보여질 url

                        //비동기방식의 요청이 들어올때는, json으로 결과를 보여줘야 하므로,
                        //컨트롤러를 작성하지 않고도 json 결과를 전송할수있다..
                        .successHandler( (request, response, auth)->{
                            response.setStatus(HttpServletResponse.SC_OK);
                            response.setContentType("application/json;charset=UTF-8");
                            /*
                            Map map = new HashMap();
                            map.put("result", "로그인성공");
                            map.put("user", auth.getName());
                            */
                            ObjectMapper objectMapper = new ObjectMapper();
                            response.getWriter().print(objectMapper.writeValueAsString(Map.of("result","로그인성공","user",auth.getName())));

                        })
                        .failureHandler( (request, response, ex)->{
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            response.setContentType("application/json;charset=UTF-8");
                            ObjectMapper objectMapper = new ObjectMapper();
                            response.getWriter().print(objectMapper.writeValueAsString(Map.of("result","로그인실패","message",ex.getMessage())));

                        })
                )
                .build();
    }

}










