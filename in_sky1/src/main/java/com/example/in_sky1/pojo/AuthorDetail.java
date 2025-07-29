package com.example.in_sky1.pojo;

import lombok.Data;

import java.io.Serializable;

@Data
public class AuthorDetail implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer id;
    private String username;
    private String picture;
    private String bio;
    private String ip;
    private Integer age;
    private Integer likeCount; //点赞数
    private Integer followerCount;  // 粉丝数
    private Integer followingCount; // 关注数


}
