package com.example.in_sky1.pojo;

import lombok.Data;

@Data
public class CacheTTL {
    // 基础时间配置（单位：秒）
    public static final int CARD_CACHE_BASE = 5 * 60; // 帖子缓存
    public static final int COMMENT_CACHE_BASE = 5 * 60; // 评论缓存
    public static final int USER_CACHE_BASE = 5 * 60; // 用户缓存
    public static final int ORDER_CACHE_BASE = 5 * 60; // 订单缓存
    public static final int USER_JWT_CACHE_BASE = 30; // JWT缓存
    // 随机波动范围（单位：秒）
    public static final int RANDOM_RANGE = 3 * 60; // 随机±2分钟
}
