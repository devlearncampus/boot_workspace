package com.sinse.jwttest.model.member;

import com.sinse.jwttest.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberDetailsService implements UserDetailsService {

    private final JpaMemberRepository jpaMemberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member=jpaMemberRepository.findById(username);

        if(member==null){
            throw new UsernameNotFoundException("로그인 정보가 올바르지 않습니다");
        }

        return new MemberDetails(member);
    }

}
