package com.sinse.customlogindb.model.member;

import com.sinse.customlogindb.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class JpaMemberDAO implements MemberDAO {
    private final JpaMemberRepository repository;

    @Override
    public Member getMemberById(String id) {
        return repository.findById(id);
    }
}
