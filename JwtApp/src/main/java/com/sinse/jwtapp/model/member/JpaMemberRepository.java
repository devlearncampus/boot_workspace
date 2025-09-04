package com.sinse.jwtapp.model.member;



import com.sinse.jwtapp.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaMemberRepository extends JpaRepository<Member, Integer> {
    //비밀번호 검증은 시큐리티가 알아서 해주므로, 개발자는 id를 이용한 회원정보 가져오는
    //메서드만 제공해주면 됨..
    public Member findById(String id);
}