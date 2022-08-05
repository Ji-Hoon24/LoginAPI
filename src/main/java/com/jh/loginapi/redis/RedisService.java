package com.jh.loginapi.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Service
public class RedisService {

    private final RedisTemplate<String, String> redisTemplate;

    @Value("${jwt.refresh.expiration}")
    private long refreshTokenValidityInSeconds;

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

}
