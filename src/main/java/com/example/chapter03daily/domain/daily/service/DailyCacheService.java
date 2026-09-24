package com.example.chapter03daily.domain.daily.service;

import com.example.chapter03daily.domain.daily.dto.DailyDetailResponse;
import com.example.chapter03daily.domain.daily.dto.DailyDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class DailyCacheService {

    private final RedisTemplate<String, Object> redisTemplate;
    private static final String CACHE_DAILY_PREFIX = "daily:";

    public DailyDetailResponse getDailyCache(long id) {
        String key = CACHE_DAILY_PREFIX + id;

        return (DailyDetailResponse) redisTemplate.opsForValue().get(key);
    }

    public void saveDailyCache(long id, DailyDetailResponse dailyDto) {
        String key = CACHE_DAILY_PREFIX + id;

        redisTemplate.opsForValue().set(key, dailyDto, 10, TimeUnit.MINUTES);
    }

}
