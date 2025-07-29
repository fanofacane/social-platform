package com.example.in_sky1.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

// 用户实体简化版
@Data
public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer id;
    private String username;
    private String picture;
    private LocalDateTime createTime;
    private Integer followerCount = 0;  // 粉丝数
    private Integer followingCount = 0; // 关注数
    private Integer likeCount;
}
