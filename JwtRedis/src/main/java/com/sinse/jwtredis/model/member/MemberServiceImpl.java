package com.sinse.jwtredis.model.member;

import com.sinse.jwtredis.domain.Member;
import org.springframework.data.jpa.repository.cdi.JpaRepositoryExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class MemberServiceImpl implements MemberService {

    private final JpaMemberRepository jpaMemberRepository;
    private final PasswordEncoder passwordEncoder;

    public MemberServiceImpl(JpaMemberRepository jpaMemberRepository, PasswordEncoder passwordEncoder) {
        this.jpaMemberRepository = jpaMemberRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void regist(Member member) throws RuntimeException {
        //평문을 암호화
        member.setPassword(passwordEncoder.encode(member.getPassword()));

        Member obj=jpaMemberRepository.save(member);
        if(obj==null) {
            throw new RuntimeException("등록실패");
        }
    }

}
