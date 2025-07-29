package com.example.in_sky1.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class ChatUserVO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer id;
    private String username;
    private String picture;
    private String content;
    private Integer unreadCount;
    private LocalDateTime createTime;
}
