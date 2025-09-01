package com.sinse.customlogindb.model.member;

import com.sinse.customlogindb.domain.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberDetailsService implements UserDetailsService {
    private final MemberDAO memberDAO;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.debug("아이디로 회원정보 조회 시도");

        Member member=memberDAO.getMemberById(username);

        if(member == null) {
            throw new UsernameNotFoundException(username);
        }

        log.debug("아이디가 발견되어서 회원정보 가져옴 ");

        //만일 회원이 존재하면, 스프링이 그 회원의 정보를 알아야 하므로
        return new CustomUserDetails(member);
    }
}
