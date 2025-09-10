package com.sinse.jwtredis.model.member;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisTokenService {

    private final StringRedisTemplate redis;

    public RedisTokenService(StringRedisTemplate redis){
        this.redis = redis;
    }

    //사용자 버전 조회 (버전이 없으면 0 을 반환)
    /*
    사용자 별로 관리하는 버전 번호를 조회하는 용도의 메서드 정의
    참고) 버전을 사용하는 이유?
        JWT+Redis 환경에서 로그아웃 처리시 블랙리스트만으로는 충분하지 않을때가 있음
        예를 들어 , 회원이 로그아웃 하거나 비밀번호를 바꿨을때, 기존에 발급된 AccessToken
        RefreshToken 을 모두 무효화 해야 함
        이때, 토큰 페이로드에 ver:1 과 같은 사용자버전을 넣어두고, Redis 에서 "uv:유저id 2"
        로 저장해놓으면 토큰의 페이로드에 갖고 있는 버전1은 Redis보다 낮으므로 , 낮은 버전을
        가진 토큰은 전부 무효화 처리할 수 있으므로
        [ 블랙리스트와 차이점 ]
        블랙리스트 방식- 토큰 단위로 차단하는 방식, 만료될때까지 redis에 개별 키보관
        버전방식 - 사용자 단위로 이 사용자가 사용중인 모든 디바이스까지 한꺼번에 토큰을 무효화
    */
    public int currentUserVersion(String userId){
        String v=redis.opsForValue().get("uv:"+userId);
        //위 메서드에 의해 redis 내부에서는 GET  uv:<userId> 가 수행 됨..
        //해당 키가 존재할 경우 저장된 문자열 그대로 반환
        //StringRedisTemplate 객체는 redis가 반환한 nil 을 java의 null로 변환
        return (v==null) ? 0 : Integer.parseInt(v);
    }
}





