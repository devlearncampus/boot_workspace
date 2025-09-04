package com.sinse.jwtlogin.domain;

//스프링 시큐리티는 회원 정보를 가진 객체를 알수없으므로, 시큐리티가 미리 정해놓은
//UserDetails안으로 Member 내용을 옮겨둬야 함

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class CustomUserDetails implements UserDetails {
    private Member member;

    public CustomUserDetails(Member member) {
        this.member = member;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getPassword() {
        return member.getPassword();
    }

    @Override
    public String getUsername() {
        return member.getId();
    }
}
