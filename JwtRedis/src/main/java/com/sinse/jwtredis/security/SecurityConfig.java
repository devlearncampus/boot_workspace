package com.sinse.jwtredis.security;

import com.sinse.jwtredis.domain.CustomUserDetails;
import com.sinse.jwtredis.filter.JwtAuthFilter;
import com.sinse.jwtredis.model.member.MemberDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;
import org.springframework.security.web.authentication.AuthenticationFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    public SecurityConfig(JwtAuthFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    //개발자가 정의한 컨트롤러에서 AuthenticationManager 를 사용할 예정이므로, 미리 등록
    //만일 개발자가 필요한 시점에 new를 해버리면, 스프링이 관리하는 Bean이 아니게 됨..
    //따라서 @Bean으로 등록해야 함
    @Bean
    public AuthenticationManager authenticationManagerBean(MemberDetailsService memberDetailsService, PasswordEncoder passwordEncoder) throws Exception {
        //AuthenticationManager는 DaoAuthenticationProvider를 통해
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(memberDetailsService); //1) 유저 얻기(by id)

        provider.setPasswordEncoder(passwordEncoder);//2) 비번 검증(using PasswordEncoder)
        return new ProviderManager(provider);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf( csrf -> csrf.disable())

                //세션기반 사용하지 않겠어!!!
                .sessionManagement(sm-> sm.sessionCreationPolicy( SessionCreationPolicy.STATELESS))

                //시큐리티에게 인증 받은 회원을 직접 알려주겠다
                .securityContext(sc -> sc.requireExplicitSave(false))

                //스프링의 디폴트 폼로그인을 비활성화시킴
                .formLogin( form->form.disable())

                //스프링의 기본 로그아웃 비활성화 시킴
                .logout( logout -> logout.disable())

                .authorizeHttpRequests( auth -> auth
                        .requestMatchers("/index.html","/member/regist.html", "/member/regist","/member/login.html","/member/login","/member/refresh" ,"/member/logout").permitAll()
                        .anyRequest().authenticated() //이외 요청은 로그인을 해야 통과..
                )

                //JWT 검증 필터를, 스프링 시큐리티의 필터 체인중 어느 부분에 관여하게 할지를 명시
                .addFilterBefore(jwtAuthFilter, AuthenticationFilter.class)

                .build();//JWT 기반이므로 CSRF 불필요
    }

}













