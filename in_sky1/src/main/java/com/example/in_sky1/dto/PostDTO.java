package com.example.in_sky1.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PostDTO {
    private Integer id;
    private Integer authorId;
    private String title;
    private String content;
    private String pictureUrl;
    private String fileType;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
