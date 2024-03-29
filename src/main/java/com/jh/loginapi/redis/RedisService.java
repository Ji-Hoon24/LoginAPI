package com.jh.loginapi.redis;

import com.jh.loginapi.config.JwtConfig;
import com.jh.loginapi.member.dto.entity.Members;
import com.jh.loginapi.member.dto.result.LoginResult;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Service
public class RedisService {

    private final RedisTemplate<String, String> redisTemplate;

    private final JwtConfig jwtConfig;

    @Value("${jwt.refresh.expiration}")
    private long refreshTokenValidityInSeconds;

    private long phoneAuthValidityInSeconds = 180000;

    private long phoneAuthSuccessValidityInSeconds = 86400000; //1일

    public void saveRefreshToken(long memberNo, String refreshToken) {
        redisTemplate.opsForValue()
            .set(String.valueOf(memberNo),
                    refreshToken,
                    refreshTokenValidityInSeconds,
                    TimeUnit.MILLISECONDS);
    }

    public String findRefreshToken(long memberNo) {
        return redisTemplate.opsForValue().get(String.valueOf(memberNo));
    }

    public void delete(String key) {
        redisTemplate.delete(key);
    }

    public void savePhoneAuthCode(String phoneNum, String authCode) {
        redisTemplate.opsForValue()
                .set(String.valueOf(phoneNum),
                        authCode,
                        phoneAuthValidityInSeconds,
                        TimeUnit.MILLISECONDS);
    }

    public String findPhoneAuthCode(String phoneNum) {
        return redisTemplate.opsForValue().get(String.valueOf(phoneNum));
    }

    public void savePhoneAuthSuccess(String phoneNum) {
        redisTemplate.opsForValue()
                .set(String.valueOf(phoneNum),
                        "Y",
                        phoneAuthSuccessValidityInSeconds,
                        TimeUnit.MILLISECONDS);
    }

    public String findPhoneAuthSuccess(String phoneNum) {
        return redisTemplate.opsForValue().get(String.valueOf(phoneNum));
    }

    @Transactional(rollbackFor = Exception.class)
    public LoginResult tokenCreate(Members members) {
        String accessToken = jwtConfig.createAccessToken(members);
        String refreshToken = jwtConfig.createRefreshToken();

        this.saveRefreshToken(members.getMemberNo(), refreshToken);
        return LoginResult.builder().accessToken(accessToken).refreshToken(refreshToken).build();
    }

}
