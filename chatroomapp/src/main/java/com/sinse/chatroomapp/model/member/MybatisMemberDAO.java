package com.sinse.chatroomapp.model.member;

import com.sinse.chatroomapp.domain.Member;
import com.sinse.chatroomapp.exception.MemberException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@Repository
public class MybatisMemberDAO implements MemberDAO {
    private MemberMapper memberMapper;

    public MybatisMemberDAO(MemberMapper memberMapper) {
        this.memberMapper = memberMapper;
    }

    @Override
    public List<Member> selectAll() {
        return memberMapper.selectAll();
    }

    @Override
    public Member select(int member_id) {
        return memberMapper.select(member_id);
    }

    @Override
    public Member login(Member member) {
        Member obj=memberMapper.login(member);
        log.debug("obj is "+obj);
        return obj;
    }

    @Override
    public void insert(Member member) throws MemberException  {
        try {
            memberMapper.insert(member);
        } catch (DataAccessException e) {
            throw new MemberException("회원 가입 실패 ",e);
        }
    }

    @Override
    public void update(Member member) throws MemberException {
        try {
            memberMapper.update(member);
        } catch (DataAccessException e) {
            throw new MemberException("회원 수정 실패 ",e);
        }
    }

    @Override
    public void delete(int member_id) throws MemberException  {
        try {
            memberMapper.delete(member_id);
        } catch (DataAccessException e) {
            throw new MemberException("회원 삭제 실패 ",e);
        }
    }
}
