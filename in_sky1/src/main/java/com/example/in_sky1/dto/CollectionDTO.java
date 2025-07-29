package com.example.in_sky1.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CollectionDTO {
    private Integer id;
    private Integer userId;
    private Integer postId;
    private LocalDateTime createTime;
}
