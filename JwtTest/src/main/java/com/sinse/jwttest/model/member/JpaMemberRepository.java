package com.sinse.jwttest.model.member;

import com.sinse.jwttest.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaMemberRepository extends JpaRepository<Member, Integer> {
    public Member findById(String id);
}

