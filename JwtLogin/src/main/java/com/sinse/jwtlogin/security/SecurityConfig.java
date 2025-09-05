package com.sinse.jwtlogin.security;


import com.sinse.jwtlogin.model.member.CustomUserDetailsService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnNotWarDeployment;
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

//스프링 시큐리티를 개발자가 원하는 설정으로 변경하고자할때 이 클래스 정의가 필요함
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    //시큐리티가 사용할 서비스 객체 등록
    private final CustomUserDetailsService customUserDetailsService;

    public SecurityConfig(CustomUserDetailsService customUserDetailsService) {
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
                )
                .build();
    }

}










