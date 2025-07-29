package com.example.in_sky1.pojo;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class Card implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer id;
    private String title;//标题
    private String pictureUrl;
    private String content;
    private String author;//作者
    private String picture;
    private Integer likesCount;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private Integer authorId;
    private Boolean isFollow;
    private Boolean isLike;
    private Boolean isCollection;
    private Integer collectionsCount;
    private Integer commentsCount;
    private String fileType;
    private Integer category;
}
