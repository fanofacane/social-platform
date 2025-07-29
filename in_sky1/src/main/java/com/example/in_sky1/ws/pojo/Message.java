package com.example.in_sky1.ws.pojo;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class Message implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer id;
    private String from;
    private String to;
    private String content;
    private LocalDateTime createTime;
}
