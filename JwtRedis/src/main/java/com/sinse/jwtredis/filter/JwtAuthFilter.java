package com.sinse.jwtredis.filter;

import com.sinse.jwtredis.model.member.RedisTokenService;
import com.sinse.jwtredis.util.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

/*
* 매 요청마다 실행되는 Jwt 인증필터
* 1) 클라이언트의 헤더에 들어있는 Authrozation 에서 Bearer 토큰 추출
* 2) JWT 서명 조작여부/만료 기간 검증 (유효한 토큰 인지 판단)
* 3) 블랙리스트 여부 검증(이미 로그 아웃한 유저인 경우 401로 전송)
* 4) 문제 없으면, 스프링 시큐리티에게 인증을 알려야 함
* */
@Slf4j
@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final RedisTokenService redis;

    public JwtAuthFilter(JwtUtil jwtUtil, RedisTokenService redis) {
        this.jwtUtil = jwtUtil;
        this.redis = redis;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try{
            /*----------------------------
            1) Authorization 헤더 추출
            ----------------------------*/
            String header = request.getHeader("Authorization");

            //넘어온 헤더값이 정상적이라면..
            if (header != null && header.startsWith("Bearer ")) {
                String token = header.substring(7); //index 7번째부터 문자열 끝까지~~모두 가져오기

                /*----------------------------
                2) JWT 파싱 및 서명조작여부, 기간만료 등을 검증
                ----------------------------*/
                Jws<Claims> jws=jwtUtil.parseToken(token);
                Claims claims=jws.getBody(); // jjwt 11.xxx , 12이상은 getPayLoad()

                /*----------------------------
                3) Claims 로 부터 필요한 값을 추출하여, 블랙리스트 여부 판단
                ----------------------------*/
                String jti=jwtUtil.getJti(claims);          //Jti - UUID 고유값
                String userId=jwtUtil.getUserId(claims);    //sub - 로그인 사용자
                int ver=jwtUtil.getVersion(claims);         //ver - 디바이스에 대한 전역적 토큰 버전


                //Redis에서 블랙리스트 조사
                if(redis.isBlackList(jti)){ //블랙리스트 인물 이라면..
                    //401 로 응답해버리면 됨..
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); //권한 없음
                    return;
                }

                //사용자 전역적 버전 검증(전역 로그아웃을 구현할 수 있음)
                int currentVer=redis.currentUserVersion(userId);//Redis
                if(currentVer!=ver){
                   //버전 불일치로 인한 401 처리
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    return;
                }

                //스프링에게 이 요청이 지금 인증을 성공했음을 알려줘야 함
                //아래의 코드를 처리해야 .anyRequest().authenticated() 를 무사히 통과
                /*
                [ details 와 principal 의 차이 ]
                principal : 누가 로그인했는가? (보통 UserDetails 또는 userId)
                credential : 어떻게 로그인했는가?(비밀번호, Jwt토큰 등)
                details : 부가적인 요청 정보 (IP, 세션ID, User-Agent 같은 환경 정보)

                */
                UsernamePasswordAuthenticationToken auth
                        =new UsernamePasswordAuthenticationToken(userId,null,
                            List.of( new SimpleGrantedAuthority("ROLE_USER"))
                        );

                SecurityContextHolder.getContext().setAuthentication(auth);
            }

            //요청 가로막앗으므로, 다시 요청의 흐름을 정상화 시킴
            //정상 로그인 인증을 받은 사람은 아래의 메서드를 통해 요청의 흐름을 타게 됨..
            filterChain.doFilter(request, response);

        }catch (ExpiredJwtException expiredJwtException){
            //만료된 토큰인 경우...
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().print("{ \"error\": \"토큰만료\"}");
        }catch(JwtException e){
            //잘못된 서명, 포맷 오류 등 기타 관련예외
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().print("{ \"error\": \"유효하지 않은 토큰\"}");
        }catch(Exception e){
            //기타 예외.
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().print("{ \"error\": \"인증 에러\"}");
        }
    }

}









