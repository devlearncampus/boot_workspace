package com.sinse.jwtredis.model.member;

import com.sinse.jwtredis.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaMemberRepository extends JpaRepository<Member, Long> {
    public Member save(Member member);
    public Member findById(String id);
}
