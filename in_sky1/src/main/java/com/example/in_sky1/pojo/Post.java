package com.example.in_sky1.pojo;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class Post implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer id;
    private User user;
    private Integer authorId;
    private String pictureUrl;
    private String content;
    private Integer likesCount;
    private  Integer collectionsCount;
    private Integer commentsCount;
    private LocalDateTime createTime;
    private String fileType;
    private State state;

}
