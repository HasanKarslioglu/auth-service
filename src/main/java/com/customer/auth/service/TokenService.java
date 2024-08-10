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

    public String getUsernameByToken(String token){
        return (String) redisTemplate.opsForValue().get(token);
    }
}
