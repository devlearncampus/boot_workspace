package com.sinse.jwtapp.model.member;


import com.sinse.jwtapp.domain.CustomUserDetails;
import com.sinse.jwtapp.domain.Member;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private JpaMemberRepository jpaMemberRepository;

    public CustomUserDetailsService(JpaMemberRepository jpaMemberRepository){
        this.jpaMemberRepository = jpaMemberRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        log.debug(username+" 사용자에 대한 loadUserByUsername 메서드 호출");

        //데이터베이스에서 해당 유저명으로 객체를 조회
        Member member=jpaMemberRepository.findById(username);

        if(member == null){
            throw new UsernameNotFoundException("로그인 정보가 올바르지 않습니다");
        }
        //하지만 코드에는 보이지 않지만, 내부적으로 DaoAuthenticationProvider가 비밀번호
        //검증을 스스로 수행함
        return new CustomUserDetails(member);
    }
}