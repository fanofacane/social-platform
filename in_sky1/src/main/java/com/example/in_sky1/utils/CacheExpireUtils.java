package com.example.in_sky1.utils;

import com.example.in_sky1.pojo.CacheTTL;
import org.springframework.stereotype.Component;

import java.util.concurrent.ThreadLocalRandom;

@Component
public class CacheExpireUtils {
    // 生成带随机偏移的过期时间（单位：秒）
    public int randomExpire(int baseTTL) {
        // 生成 -RANDOM_RANGE ~ +RANDOM_RANGE 的随机偏移量
        int randomOffset = ThreadLocalRandom.current().nextInt(
                -CacheTTL.RANDOM_RANGE, CacheTTL.RANDOM_RANGE
        );
        return baseTTL+randomOffset;
    }
}
