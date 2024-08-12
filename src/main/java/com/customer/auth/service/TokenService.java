package com.customer.auth.service;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class TokenService {

    private final RedisTemplate<String, Object> redisTemplate;

    public TokenService (RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void saveToken(String token, String username){
        redisTemplate.opsForValue().set(token, username, Duration.ofMinutes(30));
    }

    public void saveRefreshToken(String refreshToken, String username){
        redisTemplate.opsForValue().set(refreshToken, username, Duration.ofHours(8));
    }

    public String getUsernameByToken(String token){
        return (String) redisTemplate.opsForValue().get(token);
    }

    public boolean isRefreshTokenValid(String refToken){
        return  (String) redisTemplate.opsForValue().get(refToken) != null;
    }

    public void invalidateToken(String refToken){
        redisTemplate.opsForValue().getAndDelete(refToken);
    }
}
