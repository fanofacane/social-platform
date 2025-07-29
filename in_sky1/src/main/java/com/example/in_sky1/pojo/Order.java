package com.example.in_sky1.pojo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Order {
    private Long id;
    private Integer userId;
    private Integer productId;
    private Integer state;
    private Integer numbers;
    private LocalDateTime createTime;
}
