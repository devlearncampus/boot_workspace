package com.sinse.jwtredis.model.member;
import com.sinse.jwtredis.controller.dto.MemberDTO;
import com.sinse.jwtredis.domain.Member;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RegistServiceImpl implements RegistService {
    private final RegistRedisService registRedisService;

    public RegistServiceImpl(RegistRedisService registRedisService) {
        this.registRedisService = registRedisService;
    }

    @Override
    public void regist(MemberDTO memberDTO) {
        String code=registRedisService.generateCode6();
        memberDTO.setCode(code);

        //임시 회원정보를 redis에 등록
        registRedisService.savePending(memberDTO);
    }
}
