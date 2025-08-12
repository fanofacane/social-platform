package com.example.in_sky1.service;

import com.example.in_sky1.pojo.Post;

import java.util.List;
import java.util.Map;

public interface PostService {
    Map<String, Object> getUserInboxVideos(Integer count, Long lastTimestamp);
}
