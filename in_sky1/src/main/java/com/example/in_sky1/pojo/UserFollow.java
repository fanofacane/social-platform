package com.example.in_sky1.pojo;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class UserFollow implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer id;
    private Integer followerId;   // 关注者ID
    private Integer followingId;  // 被关注者ID
    private LocalDateTime createdTime;
}