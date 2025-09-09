package com.sinse.jwtredis.model.member;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sinse.jwtredis.controller.dto.MemberDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Duration;

@Slf4j
//Redis 관련 로직 : 임시가입 정보를 저장하고, 검증,
@Service
public class RegistRedisService  {
    private final StringRedisTemplate redis;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final SecureRandom random=new SecureRandom();

    //TTL(Time To Live) 설정
    private static final Duration PENDING_TTL=Duration.ofMinutes(3); //임시가입 인증 만료시간 3분

    //키설계 (redis 에 사용될 키 규칙- 개발자가 정하는 것임)
    private String pendingKey(String email){
        return "pending:"+email;  // pending:zino1187@nave.rcom , member:1001
    }

    //이메일을 조회하기 위한 키설계
    private String codeKey(String code){
        return "code:"+code;
    }

    public RegistRedisService(StringRedisTemplate redis) {
        //redis에게 데이터를 넣을때 문자열화 시켜서 넣어야 하므로, 그 문자열 작업을
        //쉽게 처리해주는 라이브러리
        this.redis = redis;
    }

    //이메일 인증코드 생성, 6자리
    public String generateCode6(){
        return String.format("%06d", random.nextInt(1_000_000)); //6자리 랜덤값
    }

    //임시가입 (redis에 쓰기)
    public void savePending(MemberDTO memberDTO){
        //Member 라는 java 객체가 redis 로 insert 되려면, 문자열화되어야 함
        try {
            String json=objectMapper.writeValueAsString(memberDTO);

            //회원임시정보
            redis.opsForValue().set(pendingKey(memberDTO.getEmail()), json, PENDING_TTL);

            //이메일을 찾을 수 있도록 인덱스를 생성하자
            redis.opsForValue().set(codeKey(memberDTO.getCode()), memberDTO.getEmail(), PENDING_TTL);

            log.debug("Redis에 등록된 이메일의 구분 코드는 "+memberDTO.getCode());

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }

}




