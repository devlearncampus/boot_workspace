package com.sinse.chatroomapp.model.member;

import com.sinse.chatroomapp.domain.Member;
import com.sinse.chatroomapp.exception.MemberException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemberServiceImpl implements MemberService {

    private MemberDAO memberDAO;
    public MemberServiceImpl(MemberDAO memberDAO) {
        this.memberDAO = memberDAO;
    }

    @Override
    public List<Member> selectAll() {
        return memberDAO.selectAll();
    }

    @Override
    public Member select(int member_id) {
        return memberDAO.select(member_id);
    }

    @Override
    public Member login(Member member) throws MemberException {
        Member obj=memberDAO.login(member);
        if(obj==null){
            throw new MemberException("회원정보가 존재하지 않습니다");
        }
        return obj;
    }

    @Override
    public void insert(Member member)  throws MemberException{
        memberDAO.insert(member);
    }

    @Override
    public void update(Member member) throws MemberException {
        memberDAO.update(member);
    }

    @Override
    public void delete(int member_id) throws MemberException {
        memberDAO.delete(member_id);
    }
}
