package com.example.in_sky1.mapper;

import com.example.in_sky1.pojo.Card;
import com.example.in_sky1.pojo.Like;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface LikeMapper {

    boolean checkLikeExists(Integer userId, Integer postId, Integer commentId);

    void deleteLike(Integer userId, Integer postId, Integer commentId);

    void insertLike(Like like);

    List<Card> getLikeListByUserId(Integer userId);
}
