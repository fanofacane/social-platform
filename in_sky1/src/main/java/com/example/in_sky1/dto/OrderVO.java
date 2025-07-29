package com.example.in_sky1.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OrderVO {
    private String id;
    private Integer userId;
    private Integer productId;
    private String title;
    private String picture;
    private Integer state;
    private Integer numbers;
    private Integer price;
    private LocalDateTime createTime;
}
