package com.sinse.customlogindb.model.member;

import com.sinse.customlogindb.domain.Member;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.FluentQuery;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public interface JpaMemberRepository extends JpaRepository<Member, Integer> {
    //이미 수많은 CRUD메서드가 지원되지만...개발자가 원하는 메서드로 커스텀 하려면
    //여기에 새로운 정의를 하면 된다..
    public Member findById(String id);
    public Member findByPassword(String password);
}
