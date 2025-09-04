package com.sinse.jwtapp.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sinse.jwtapp.model.member.CustomUserDetailsService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.NullSecurityContextRepository;

import java.util.HashMap;
import java.util.Map;

//스프링 시큐리티를 개발자가 원하는 설정으로 변경하고자할때 이 클래스 정의가 필요함
public class FormLogfinSecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;

    public FormLogfinSecurityConfig(CustomUserDetailsService customUserDetailsService) {
        this.customUserDetailsService = customUserDetailsService;
    }

    ObjectMapper objectMapper = new ObjectMapper();

    //현재 db에 들어간 비밀번호는 BCrypt 알고리즘이므로, 시큐리티에게 같은 알고리즘을 사용하도
    //록 알려줘야 함
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf ->csrf.disable())

                //세션 로그인을 안하기 위한 설정
                .sessionManagement(session ->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .securityContext(sc->sc.securityContextRepository(new NullSecurityContextRepository()))


                //폼로그인 설정 (이 설정이 되어 있으면 컨트롤러로 요청을 직접 받지 않게 됨)
                .formLogin( form -> form
                        .loginProcessingUrl("/member/login")
                        .usernameParameter("username")
                        .passwordParameter("password")

                        //비동기 방식 요청 시 응답 데이터를 json 으로 받는 방법
                        .successHandler((request, response, authentication) -> {
                            response.setContentType("application/json;charset=UTF-8");
                            var writer = response.getWriter();

                            Map map = new HashMap();
                            map.put("result","success");
                            map.put("user",authentication.getName());
                            ObjectMapper objectMapper = new ObjectMapper();
                            writer.write(objectMapper.writeValueAsString(map));
                            writer.flush();
                        })
                        .failureHandler((request, response, exception) -> {
                            response.setContentType("application/json;charset=UTF-8");
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            var writer = response.getWriter();
                            Map map = new HashMap();
                            map.put("result","fail");
                            map.put("message", exception.getMessage());

                            writer.write(objectMapper.writeValueAsString(map));

                            writer.flush();
                        })
                )
                .build();
    }


    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(UserDetailsService uds, PasswordEncoder encoder) {
        DaoAuthenticationProvider p = new DaoAuthenticationProvider();
        p.setUserDetailsService(customUserDetailsService);   // 여기서 UDS를 사용해도 세션은 안 만듭니다
        p.setPasswordEncoder(encoder);
        return p;
    }
    //AuthenticationManager 등록
    @Bean
    public AuthenticationManager authenticationManagerBean(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
}