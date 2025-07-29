package com.example.in_sky1.utils;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class RedisIdWorker {
    private static final Long BEGIN_TIMESTAMP= 1640995200L;
    private static final int COUNT_BITS= 32;
    private StringRedisTemplate stringRedisTemplate;
    public RedisIdWorker(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }
    public long nextId(String key) {
        //生成时间戳
        LocalDateTime now = LocalDateTime.now();
        Long newSeconds = now.toEpochSecond(ZoneOffset.UTC);
        Long timestamp = newSeconds-BEGIN_TIMESTAMP;
        //生成序列号
        String date=now.format(DateTimeFormatter.ofPattern("yyyy:MM:dd"));
        Long count = stringRedisTemplate.opsForValue().increment("icr:" + key + ":" + date);
        //拼接返回
        return timestamp << COUNT_BITS | count;
    }
}
