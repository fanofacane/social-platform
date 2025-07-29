package com.example.in_sky1.service;

import com.example.in_sky1.pojo.Post;

import java.util.List;

public interface PostService {
    List<Post> getPostsByFollowed();
}
