package com.sinse.jwtapp.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sinse.jwtapp.filter.JwtAuthFilter;
import com.sinse.jwtapp.model.member.CustomUserDetailsService;
import com.sinse.jwtapp.util.JwtUtil;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.NullSecurityContextRepository;
import org.springframework.web.filter.OncePerRequestFilter;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableWebSecurity
//스프링 시큐리티를 개발자가 원하는 설정으로 변경하고자할때 이 클래스 정의가 필요함
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;

    ObjectMapper objectMapper = new ObjectMapper();


    public SecurityConfig(CustomUserDetailsService customUserDetailsService){
        this.customUserDetailsService = customUserDetailsService;
    }

    @Bean
    public OncePerRequestFilter jwtAuthFilter(JwtUtil jwtUtil, UserDetailsService uds) {
        return new JwtAuthFilter(jwtUtil, uds);
    }

    //현재 db에 들어간 비밀번호는 BCrypt 알고리즘이므로, 시큐리티에게 같은 알고리즘을 사용하도
    //록 알려줘야 함
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(UserDetailsService uds, PasswordEncoder encoder) {
        DaoAuthenticationProvider p = new DaoAuthenticationProvider();
        p.setUserDetailsService(uds);   // 여기서 UDS를 사용해도 세션은 안 만듭니다
        p.setPasswordEncoder(encoder);
        return p;
    }
    //AuthenticationManager 등록
    @Bean
    public AuthenticationManager authenticationManagerBean(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   OncePerRequestFilter jwtAuthFilter, //매 요청마다 한 번씩 실행되는 커스텀 Jwt 인증 필터를 주입받음
                                                   DaoAuthenticationProvider daoAuthProvider //UserDetailsService 와 PasswordEncoder 를 이용해 아이디/비밀번호 검증을 수행하는
                                                                                            //프로바이더 , AuthenticationManager의 authenticate() 를 호출시 동작함
                                        ) throws Exception {
        http
            .csrf(csrf ->csrf.disable()) //CSRF 보호기능 비활성화 - 전통적인 폼 세션 로그인이 아니라 REST/JWT로만 처리할때 보통 끔

            //세션을 만들지도, 사용하지도 않도록 정책을 STATELESS 로 설정, 시큐리티가 인증 상태를 세션에 저장하지않게 됨
            .sessionManagement(session ->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

            //SecurityContext 저장소를 Null 저장소로 교체함, 즉 인증 결과를 세션/서버 상태에 저장하지 않음 ( 완전 무상태 보장에 도움)
            .securityContext(sc->sc.securityContextRepository(new NullSecurityContextRepository()))



            .formLogin(AbstractHttpConfigurer::disable)//시큐리티의 기본 폼 로그인필터인 UsernamePasswordAuthenticationFilter 를 끄는 설정
                                                        // 개발자가 직접 정의한 Controller의 로그인 API가 호출됨

            .logout(AbstractHttpConfigurer::disable) //시큐리티 기본 로그아웃 처리(세션 무효화/쿠키 삭제 리다이렉트 등) 를 끄는 설정, JWT 환경에서는
                // 보통 서버 상태가 없으므로, 수동으로 쿠키 삭제, 블랙리스트 등으로 처리함

            .authorizeHttpRequests(auth->auth
                    //스프링 부트의 정적 리소스 위치를 인증없이 모두 허용 (css/js/images/webjars 등)
                    .requestMatchers(org.springframework.boot.autoconfigure.security.servlet.PathRequest.toStaticResources().atCommonLocations()).permitAll()

                    .requestMatchers("/member/login.html").permitAll() //로그인 페이지 접근을 누구나 허용
                    .requestMatchers("/member/login").permitAll() //로그인 처리 API를 누구나 호출가능하게 함
                    .anyRequest().authenticated() //위에서 허용한 경로를 제외한 모든 요청은 인증을 필요로 하는 것으로 설정함
            )

            // Provider를 체인에 명시 등록(명확·안전)
            .authenticationProvider(daoAuthProvider) // 이 보안 치엔에서 사용할 DaoAuthenticationProvider 를 등록
                                                    //  Controller에서 AuthenticationManager.authenticate() 메서드를 호출하는 순간 이 프로바이더가 자동으로
                                                    // UserDetailsService.loadUserByUsername() 메서드와 PasswordEncoder.matches() 를 통해 아이디 및 비밀번호 검증을 수행

            .addFilterBefore(jwtAuthFilter, AuthorizationFilter.class); //권한 심사 필터(AuthorizationFilter)가 실행되기 전에 jwtAuthFilter 를 앞에 배치함
            // 먼저 JWT로 SecurityContext 를 채워두고, 그 다음에 인가 판단을 받도록 보장함
            // 폼 로그인을 껐기 때문에 UsernamePasswordAuthenticationFilter 는 없을 수 있어서, AuthorizationFilter 기준이 더 안전함)
        return http.build(); //위 연결된 설정으로 SecurityFilterChain 을 생성하여 스프링 컨테이너에 반환함, 이 체인이 애플리케이션의 요청 보안 파이프라인으로 동작하게 됨
    }
}
/* 동작 흐름 요약
    1) 클라이언트가 /member/login 으로 아이디,비번을 전송
    2) Controller 에서 AuthenticationManager.authenticate() 메서드 호출
    3) 내부적으로 DaoAuthenticationProvider가 사용자 조회 + 비번 검증 수행
    4) 성공 시 Controller가 JWT 발급해서 응답
    5) 로그인 이후, 보호 API 접근 시 jwtAuthFilter가 매 요청의 Authorization: Bearer ...를 검증하고 SecurityContext 세팅
    6) AuthorizationFilter가 인가 판단
    7) 통과 시 Controller 로직 실행
*/