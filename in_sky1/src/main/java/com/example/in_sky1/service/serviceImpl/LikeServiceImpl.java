package com.example.in_sky1.service.serviceImpl;

import com.example.in_sky1.mapper.*;
import com.example.in_sky1.pojo.Card;
import com.example.in_sky1.pojo.Like;
import com.example.in_sky1.pojo.User;
import com.example.in_sky1.service.LikeService;
import com.example.in_sky1.utils.CurrentHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class LikeServiceImpl implements LikeService {
    @Autowired
    private LikeMapper likeMapper;
    @Autowired
    private PostMapper postMapper;
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private CardMapper cardMapper;

    @Override
    public String toggleLike(Like like) {
        boolean exists = likeMapper.checkLikeExists(like.getUserId(), like.getPostId(), like.getCommentId());
        String key="cardLikedCache::"+like.getPostId()+like.getCommentId();
        if (exists) {
            likeMapper.deleteLike(like.getUserId(), like.getPostId(), like.getCommentId());
            redisTemplate.opsForSet().remove(key,like.getUserId().toString());
            updateLikeCount(like.getPostId(), like.getCommentId(), -1);
        } else {
            like.setCreateTime(LocalDateTime.now());
            likeMapper.insertLike(like);
            redisTemplate.opsForSet().add(key,like.getUserId().toString());
            updateLikeCount(like.getPostId(), like.getCommentId(), 1);
        }
        return "success";
    }

    //点赞状态
    @Override
    public boolean Liked(Integer postId, Integer commentId) {
        Integer userId= CurrentHolder.getCurrentId();
        boolean exists = likeMapper.checkLikeExists(userId, postId, commentId);
        if (exists) {
            return true;
        }
        return false;
    }

    @Override
    public List<Card> getLikeListByUserId() {
        Integer userId= CurrentHolder.getCurrentId();
        List<Card> cardList=likeMapper.getLikeListByUserId(userId);
        for (Card card : cardList) {
            User user=userMapper.selectUserById(card.getAuthorId());
            card.setPicture(user.getPicture());
            card.setAuthor(user.getUsername());
            card.setIsLike(true);
        }
        return cardList;
    }

    private void updateLikeCount(Integer postId, Integer commentId, int count) {
        if (postId != null) {
            postMapper.updatePostLikes(postId, count);
        } else if (commentId != null) {
            commentMapper.updateCommentLikes(commentId, count);
        }
    }
}
