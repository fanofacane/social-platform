package com.example.in_sky1.service;

import com.example.in_sky1.pojo.Card;
import com.example.in_sky1.pojo.Like;

import java.util.List;

public interface LikeService {
    String toggleLike(Like like);

    boolean Liked(Integer postId, Integer commentId);

    List<Card> getLikeListByUserId();
}
