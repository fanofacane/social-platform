package com.example.in_sky1.ws.pojo;

import lombok.Data;

import java.time.LocalDateTime;
@Data
public class MessageVo {
    private Integer id;
    private String content;
    private String to;
    private String from;
    private String order;
    private LocalDateTime time;
}
