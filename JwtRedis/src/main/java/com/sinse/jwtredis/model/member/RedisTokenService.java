package com.sinse.jwtredis.model.member;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

/*
    [ Redis 3.x 기반 토큰 상태 관리 ]
    1) 로그아웃 한 유저를 어떤 방식으로 블랙리스트에 저장할 것인지 키에 대한 설계
        bl:access:<Jti>
    2) 사용자의 모든 디바이스로부터 로그아웃을 수행하기 위한 버전 키값에 대한 설계
        uv:<userId>
    3) access 토큰과 함께 발급되는 refresh 토큰을 저장하기 위한 키값에 대한 설계
        rt:<userId>:<deviceId>
*/

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

    //AccessToken 을 블랙리스트 등록
    //ttlSeconds 토큰에 남은 잔여 시간을 등록해야 함
    public void registBlackList(String jti, long ttlSeconds){
        //ttlSeconds이 이미 만료된 시간인 경우엔 블랙리스트에 등록할 필요조차 없다
        if(ttlSeconds<=0)return;

        // SETEX bl:access:jti 시간 값
        redis.opsForValue().set("bl:access:"+jti,"1", Duration.ofSeconds(ttlSeconds));
    }

    //블랙리스트 조회
    public boolean isBlackList(String jti){
        Boolean exists=redis.hasKey("bl:access:"+jti);
        //위의 hasKey() 메서드에 의해 수행되는 Redis 명령어는
        // EXISTS  bl:access:abc123
        // 1 -> 키가 존재
        // 0-> 키가 없음
        //Spring data redis는 이 반환값을 Boolean 값으로 자동 변경해줌

        return exists !=null && exists;
    }
}





