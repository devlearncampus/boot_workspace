package com.sinse.customlogindb.model.member;

import com.sinse.customlogindb.domain.Member;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.FluentQuery;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public interface JpaMemberRepository extends JpaRepository<Member, Integer> {
    public Member findById(String id);
}
